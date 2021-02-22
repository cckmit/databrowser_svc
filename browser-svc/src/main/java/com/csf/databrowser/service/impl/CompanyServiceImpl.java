package com.csf.databrowser.service.impl;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.csf.databrowser.convert.ValueConvert;
import com.csf.databrowser.dao.DsMetricsMapDao;
import com.csf.databrowser.dao.DsPublicDao;
import com.csf.databrowser.entity.DsExportRecord;
import com.csf.databrowser.entity.DsMetricsMap;
import com.csf.databrowser.excel.CompanyWorkbookRead;
import com.csf.databrowser.excel.DataSheetTemplate;
import com.csf.databrowser.excel.WorkbookBuilder;
import com.csf.databrowser.extract.ExcelHelper;
import com.csf.databrowser.request.CommonMetriesReq;
import com.csf.databrowser.request.ExtractRequest;
import com.csf.databrowser.resp.*;
import com.csf.databrowser.service.CompanyInfoService;
import com.csf.databrowser.service.CompanyService;
import com.csf.databrowser.service.CompanyStdService;
import com.csf.databrowser.service.DsExportSerivce;
import com.csf.risk.common.util.RiskUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.csf.risk.common.util.RiskUtil.isIllegalException;

@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    @Resource
    private CompanyInfoService companyInfoService;
    @Resource
    private CompanyStdService companyStdService;

    @Autowired
    private DsMetricsMapDao dsMetricsMapDao;
    @Autowired
    private DsPublicDao dsPublicDao;
    @Autowired
    private DsExportSerivce dsExportSerivce;

    @Value("${company.limit:100}")
    private Integer companyLimit;

    @Value("${company.sheet.name:}")
    private String companySheetName;

    protected String csfIdNameAlias = "csfId";
    private String BASE_STOCK_TABLE = "base_stock";
    private static Map<String, String> tableToCsfId = new HashMap<>();
    private AtomicBoolean loadConvert = new AtomicBoolean(false);
    private Map<String, ValueConvert> valueConverts = new HashMap<>();
    protected ApplicationContext applicationContext;
    private Map<String, ValueConvert> metriesValueConvert = new ConcurrentHashMap<>();

    static {
        tableToCsfId.put("company_std", "csf_id");
        tableToCsfId.put("base_stock", "csfid");
        tableToCsfId.put("company_business_scope", "csf_id");
        tableToCsfId.put("base_shareholder_def", "csfid");
        tableToCsfId.put("company_change_basic", "csf_id");
        tableToCsfId.put("company_illegal", "csf_id");
        tableToCsfId.put("company_investor", "csf_id");
        tableToCsfId.put("company_staff", "csf_id");
    }

    @Override
    public CompaniesInfoResp getCompanyInfo(String keyword, Integer page, Integer size) {
        CompaniesInfoResp resp = new CompaniesInfoResp();
        if (StringUtils.isBlank(keyword)) {
            return resp;
        }
        CompaniesESResp companies = companyInfoService.getCompanyInfo(keyword, page,size);
        if (ObjectUtils.isEmpty(companies.getResult())){
            return resp;
        }
        List<CompanyESResp> esRespList = companies.getResult();
        resp = generateCompaniesInfoResp(esRespList);
        return resp;
    }

    @Override
    public CompanySearchResp searchCompanys(InputStream is) {
        CompanyWorkbookRead read = new CompanyWorkbookRead(is);
        List<String> companies = read.read(companySheetName);
        //limit
        companies = companies.size() > companyLimit? companies.subList(0, companyLimit): companies;
        CompanySearchResp resp = new CompanySearchResp();
        resp.setResult(companies.parallelStream().map(x -> {
            try {
                CompaniesESResp result = companyInfoService.getCompanyInfo(x, 0, 20);
                if (result != null && !CollectionUtils.isEmpty(result.getResult())) {
                    CompanyESResp company = result.getResult().get(0);
                    for (CompanyESResp com : result.getResult()) {
                        if (Objects.equals(com.getName(), x)) {
                            company = com;
                            break;
                        }
                    }
                    return convertToCompanyInfoResp(company);
                }
            }catch (Exception e) {
               log.error(String.format("search es company %s info error", x), e);
            }
            return null;
        }).filter(x -> x != null).collect(Collectors.toList()));
        return resp;
    }

    @Override
    public CompaniesExtractResp extract(ExtractRequest request) {
        CompaniesExtractResp respList = new CompaniesExtractResp();
        if (ObjectUtils.isEmpty(request)){
            return respList;
        }
        respList = getCompanyExtract(request);
        return respList;
    }

    @Override
    public String download(HttpServletRequest request) {
        List<CompanyExtractResp> list = new ArrayList<>();
        return "ok";
    }

    @Override
    public DataRes getPageData(CommonMetriesReq req) {
        DataRes res = new DataRes();
        List<Map<String,Object>> result = getData(req);
        if (CollectionUtils.isEmpty(result)){
            return res;
        }
        int satrt = req.getPage()*req.getSize();
        res.setTotal(result.size());
        res.setData(result.stream().skip(satrt).limit(req.getSize()).collect(Collectors.toList()));
        return res;
    }

    @Override
    public void exportCompanyInfo(CommonMetriesReq req, HttpServletResponse response, HttpServletRequest request) {
        exportDataInfo(req, response, request, "公司基本信息");
    }

    private void exportDataInfo(CommonMetriesReq req, HttpServletResponse response,
                                HttpServletRequest request, String sheetName) {
        long start = System.currentTimeMillis();
        List<Map<String, Object>> result = getData(req);
        String[] headers = getTitles(req.getMetricsInfos());
        String[] metricsCodes = getMetrics(req.getMetricsInfos());
        List<List<Object>> contentList = getContent(result, req.getMetricsInfos());

        DsExportRecord record = new DsExportRecord();
        int fileSize = exportData(response, contentList, headers, sheetName);

        record.setFileSize(fileSize);
        record.setUsername(RiskUtil.getUsername());
        record.setCsfIds(String.join(",", req.getCsfIds()));
        record.setUrl(request.getRequestURI());
        record.setMetrics(String.join(",", metricsCodes));
        record.setDuration(System.currentTimeMillis() - start);
        dsExportSerivce.addExportData(record);
    }

    private int exportData(HttpServletResponse response, List<List<Object>> contentList, String[] headers, String sheetName) {
        response.setContentType("application/ms-excel");
        response.setCharacterEncoding("UTF-8");
        int size = 0;
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(getExportFileName(), "UTF-8"));
            WorkbookBuilder workbookBuilder = new WorkbookBuilder(response.getOutputStream());
            workbookBuilder.addSheet(new DataSheetTemplate(contentList, headers, sheetName));
            size = workbookBuilder.build();
        } catch (Exception e) {
            log.error("export error", e);
        }
        return size;
    }

    private String getExportFileName() {
        return "公司基本信息导出.xlsx";
    }

    private List<List<Object>> getContent(List<Map<String, Object>> result, List<CommonMetriesReq.MetricsInfo> metricsInfos) {
        if(CollectionUtils.isEmpty(result)) {
            return Collections.EMPTY_LIST;
        }
        List<List<Object>> contentList = new ArrayList<>(result.size());
        for(Map<String, Object> value : result) {
            List<Object> list = new ArrayList<>();
            for (CommonMetriesReq.MetricsInfo info : metricsInfos) {
                list.add(value.get(info.getMetricsCode()) != null ? value.get(info.getMetricsCode()) : "");
            }
            contentList.add(list);
        }
        return contentList;
    }

    private String[] getMetrics(List<CommonMetriesReq.MetricsInfo> metricsInfos) {
        String[] metricsCodes = new String[metricsInfos.size()];
        for (int i = 0; i < metricsCodes.length; i++) {
            metricsCodes[i] = metricsInfos.get(i).getMetricsCode();
        }
        return metricsCodes;
    }

    private String[] getTitles(List<CommonMetriesReq.MetricsInfo> metricsInfos) {
        String[] headers = new String[metricsInfos.size()];
        for (int i = 0; i < headers.length; i++) {
            headers[i] = metricsInfos.get(i).getTitle();
        }
        return headers;
    }

    private List<Map<String, Object>> getData(CommonMetriesReq req) {

        //查询指标数据
        List<CommonMetriesReq.MetricsInfo> metricsInfos = req.getMetricsInfos();
        //将MetricsCode作为key得到map集合
        Map<String, CommonMetriesReq.MetricsInfo> metricsInfoMap = metricsInfos.stream()
                .collect(Collectors.toMap(x -> x.getMetricsCode(), y -> y, (v1, v2) -> v2));
        //从数据库中查住指标所在的表
        List<DsMetricsMap> result = dsMetricsMapDao.getMetricsMap(metricsInfoMap.keySet());
        if (CollectionUtils.isEmpty(result) || result.size() != metricsInfos.size()) {
            isIllegalException(true, "metricsInfos");
        }
        //根据list的值进行分组
        Map<Integer, List<DsMetricsMap>> listMap = result.stream().collect(Collectors.groupingBy(DsMetricsMap::getList));
        //公告指标
        List<DsMetricsMap> list0 = listMap.get(0) != null ? listMap.get(0) : new ArrayList<>();
        //上市指标
        List<DsMetricsMap> list1 = listMap.get(1) != null ? listMap.get(1) : new ArrayList<>();
        //非上市指标
        List<DsMetricsMap> list2 = listMap.get(2) != null ? listMap.get(2) : new ArrayList<>();

        //将指标根据不同表进行分组
        Map<String, List<DsMetricsMap>> map0 = groupByTable(list0);
        Map<String, List<DsMetricsMap>> map1 = groupByTable(list1);
        Map<String, List<DsMetricsMap>> map2 = groupByTable(list2);
        //根据CsfId去各个表查询对应的数据
        Map<String, List<Map<String, Object>>> result0 = getTableData(map0, req.getCsfIds());
        Map<String, List<Map<String, Object>>> result1 = getTableData(map1, req.getCsfIds());
        Map<String, List<Map<String, Object>>> result2 = getTableData(map2, req.getCsfIds());
        //组装返回数据
        return getData(result0, result1, result2, req);
    }

    private List<Map<String, Object>> getData(Map<String, List<Map<String, Object>>> result0,
                                              Map<String, List<Map<String, Object>>> result1,
                                              Map<String, List<Map<String, Object>>> result2,
                                              CommonMetriesReq req) {
        Map<String, Map<String, Object>> map = new HashMap<>();
        if (CollectionUtils.isEmpty(result0)) {
            return Collections.EMPTY_LIST;
        }
        //合并result0
        combinTable(map, result0);

        //处理特殊表base_stock
        combinBaseStock(result1);

        //合并result1
        combinTable(map, result1);

        //合并result2
        combinTable(map, result2);

        List<Map<String, Object>> result = new ArrayList<>(map.values());
        //转化
        convert(req.getMetricsInfos(), result);
        //排序
        result.sort((x, y) -> ((String) x.get(csfIdNameAlias)).compareTo((String) y.get(csfIdNameAlias)));
        return result;
    }

    private void combinBaseStock(Map<String, List<Map<String, Object>>> result1) {
        List<Map<String, Object>> baseStock = result1.get(BASE_STOCK_TABLE);
        if (baseStock != null){
            Map<String,Map<String,Object>> tmp = new HashMap<>();
            for (Map<String, Object> map : baseStock) {
                Map<String, Object> target = tmp.get(map.get(csfIdNameAlias));
                if (target == null){
                    target = map;
                    tmp.put((String) map.get(csfIdNameAlias),target);
                    continue;
                }
                //将csfId相同的数据进行合并
                for (Map.Entry<String, Object> entry : target.entrySet()) {
                    if (!entry.getKey().equals(csfIdNameAlias)){
                        String val = (String)target.get(entry.getKey());
                        if (val==null){
                            target.put(entry.getKey(),","+entry.getValue());
                        }else if (entry.getKey()!= null && !val.contains((String)entry.getValue())){
                            target.put(entry.getKey(), val + "," + entry.getValue());
                        }else if (entry.getValue() == null){
                            target.put(entry.getKey(), val + ",");
                        }
                    }
                }
            }
            result1.put(BASE_STOCK_TABLE, new ArrayList<>(tmp.values()));
        }
    }

    protected  void convert(List<CommonMetriesReq.MetricsInfo> metricsInfos, List<Map<String, Object>> result) {
        //转化
        List<CommonMetriesReq.MetricsInfo> filterMetricsInfos = metricsInfos.stream().filter(x -> x.getOutputInfo() != null).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(metricsInfos)) {
            Map<String, ValueConvert> valueConverts = getValueConverts(filterMetricsInfos);
            for (Map<String, Object> entity : result) {
                for (CommonMetriesReq.MetricsInfo metricsInfo : filterMetricsInfos) {
                    ValueConvert valueConvert = valueConverts.get(metricsInfo.getKey());
                    Object value = entity.get(metricsInfo.getMetricsCode());
                    if (value != null) {
                        entity.put(metricsInfo.getMetricsCode(), valueConvert.transfer(value));
                    }
                }
            }
        }
    }

    private Map<String, ValueConvert> getValueConverts(List<CommonMetriesReq.MetricsInfo> metricsInfos) {
        Map<String, ValueConvert> map = new HashMap<>();
        if (CollectionUtils.isEmpty(metricsInfos)) {
            return map;
        }

        if (!loadConvert.get()) {
            synchronized (this) {
                if (!loadConvert.get()) {
                    valueConverts = applicationContext.getBeansOfType(ValueConvert.class);
                    loadConvert.set(true);
                }
            }
        }

        if (CollectionUtils.isEmpty(valueConverts)) {
            return map;
        }

        for (CommonMetriesReq.MetricsInfo info : metricsInfos) {
            if (metriesValueConvert.containsKey(info.getKey())) {
                map.put(info.getKey(), metriesValueConvert.get(info.getKey()));
                continue;
            }

            for (Map.Entry<String, ValueConvert> entry : valueConverts.entrySet()) {
                if (entry.getValue().match(info)) {
                    map.put(info.getKey(), entry.getValue());
                    metriesValueConvert.put(info.getKey(), entry.getValue());
                    break;
                }
            }
        }
        return map;
    }

    private void combinTable(Map<String, Map<String, Object>> map, Map<String, List<Map<String, Object>>> result) {
        for(List<Map<String, Object>> list : result.values()) {
            for (Map<String, Object> source : list) {
                String csfId = source.get(csfIdNameAlias).toString();
                Map<String, Object> target = map.get(csfId);
                if (target == null) {
                    target = new HashMap<>();
                    map.put(csfId, target);
                }
                copy(source, target);
            }
        }
    }

    private void copy(Map<String, Object> source, Map<String, Object> target) {
        source.entrySet().forEach(x -> {
            target.put(x.getKey(), x.getValue());
        });
    }

    private Map<String, List<Map<String, Object>>> getTableData(Map<String, List<DsMetricsMap>> map, List<String> csfIds) {
        Map<String, List<Map<String, Object>>> result = new ConcurrentHashMap<>();
        map.entrySet().parallelStream().forEach(x -> {
            result.put(x.getKey(), dsPublicDao.selectPublicItemList(generateSql(x.getKey(), x.getValue(), csfIds)));
        });
        return result;
    }

    private String generateSql(String key, List<DsMetricsMap> value, List<String> csfIds) {
        StringBuilder sb = new StringBuilder("select ");
        for (int i = 0; i < value.size(); i++) {
            sb.append(value.get(i).getColumnName()+" as "+value.get(i).getMetricCode()+",");
        }
        String csfIdName = tableToCsfId.get(key);
        if (csfIdName == null) {
            isIllegalException(true, key + " csfId not exists");
        }
        sb.append(csfIdName + " as " + csfIdNameAlias + " from " + key + " where " + csfIdName + " in (");
        int count = 0;
        for(String csfId : csfIds) {
            count++;
            sb.append("'" + csfId + "'");
            if(count < csfIds.size()) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    private Map<String, List<DsMetricsMap>> groupByTable(List<DsMetricsMap> data) {
        return data.stream().collect(Collectors.groupingBy(x -> x.getTableName()));
    }

    private CompaniesExtractResp getCompanyExtract(ExtractRequest request) {
        CompaniesExtractResp respList = new CompaniesExtractResp();
        List<MetricsResp> metrics = request.getMetrics();
        List<CompanyExtractResp> extractResp = request.getCsfIdList().stream().map(re -> companyStdService.getCompanyExtract(re,metrics)).collect(Collectors.toList());
        respList.setExtractRespList(extractResp);
        //导出excel
        List<String> nameList = request.getMetrics().stream().map(MetricsBaseResp::getMetricsCode).collect(Collectors.toList());
        try {
            downloadExcel(((CompaniesExtractResp)(respList.clone())).getExtractRespList(),nameList);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return respList;
    }

    public void downloadExcel(List<CompanyExtractResp> ExtractResp,List<String> nameList) {
        ExportParams exportParams = new ExportParams("公司信息","导出结果");
        ExcelHelper.getExcel(nameList, CompanyExtractResp.class);
        Workbook sheets = ExcelExportUtil.exportExcel(exportParams,CompanyExtractResp.class, ExtractResp);;
        try {
            OutputStream n= new FileOutputStream("C:\\Users\\robin.wang_ext\\Desktop\\公司.xls");
            sheets.write(n);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CompaniesInfoResp generateCompaniesInfoResp(List<CompanyESResp> esRespList) {
        List<CompanyInfoResp> result = esRespList.stream().map(es -> convertToCompanyInfoResp(es))
                .collect(Collectors.toList());
        CompaniesInfoResp resp = new CompaniesInfoResp();
        resp.setResult(result);
        return resp;
    }

    private CompanyInfoResp convertToCompanyInfoResp(CompanyESResp resp){
      return new CompanyInfoResp(
              resp.getCsfId(),
              resp.getName(),
              CollectionUtils.isEmpty(resp.getSecurities()) ? null:resp.getSecurities().get(0).getSecu(),
              CollectionUtils.isEmpty(resp.getSecurities()) ? null:resp.getSecurities().get(0).getTick(),
              null);
    }

}
