package com.csf.databrowser.service.impl;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.csf.databrowser.excel.CompanyWorkbookRead;
import com.csf.databrowser.extract.ExcelHelper;
import com.csf.databrowser.request.ExtractRequest;
import com.csf.databrowser.resp.*;
import com.csf.databrowser.service.CompanyInfoService;
import com.csf.databrowser.service.CompanyService;
import com.csf.databrowser.service.CompanyStdService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    @Resource
    private CompanyInfoService companyInfoService;
    @Resource
    private CompanyStdService companyStdService;

    @Value("${company.limit:100}")
    private Integer companyLimit;

    @Value("${company.sheet.name:}")
    private String companySheetName;

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
