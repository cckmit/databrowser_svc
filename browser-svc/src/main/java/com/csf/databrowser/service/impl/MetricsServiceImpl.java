package com.csf.databrowser.service.impl;

import com.csf.databrowser.dao.DsCombineMetricsDao;
import com.csf.databrowser.dao.DsMetricsDao;
import com.csf.databrowser.entity.DsCombineMetrics;
import com.csf.databrowser.entity.DsMetrics;
import com.csf.databrowser.entity.DsMetricsByModule;
import com.csf.databrowser.resp.MetricsBaseResp;
import com.csf.databrowser.resp.MetricsResp;
import com.csf.databrowser.resp.ModuleMetricsResp;
import com.csf.databrowser.service.MetricsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author eric.yao
 * @date 2021/1/28
 **/
@Slf4j
@Service
public class MetricsServiceImpl implements MetricsService {

    @Resource
    private DsMetricsDao dsMetricsDao;

    @Resource
    private DsCombineMetricsDao combineMetricsDao;

    @Override
    public List<ModuleMetricsResp> findModuleMetrics(Integer moduleId) {

        StopWatch watch = new StopWatch("findModuleMetrics");
        watch.start();
        List<DsMetricsByModule> modules = dsMetricsDao.findMetricsAndGroupByModule(moduleId);

        Map<String,List<DsMetricsByModule>> groups = modules.stream().collect(Collectors.groupingBy(DsMetricsByModule::getGroupCode));


        List<ModuleMetricsResp> resp = new ArrayList<>();
        for (Map.Entry<String, List<DsMetricsByModule>> entry : groups.entrySet()) {
            ModuleMetricsResp res = new ModuleMetricsResp();
            res.setGroupCode(entry.getKey());
            List<DsMetricsByModule> values = entry.getValue();
            if (CollectionUtils.isNotEmpty(values)){
                res.setGroupName(values.get(0).getGroupName());
            }

            res.setMetrics(values.stream().map(this::convert).collect(Collectors.toList()));
            resp.add(res);
        }
        watch.stop();
        if (log.isDebugEnabled()){
            log.debug(watch.prettyPrint());
        }
        return resp;
    }

    @Override
    public List<MetricsBaseResp> findCombineMetrics(String metricsCode) {
        List<DsCombineMetrics> metrics = combineMetricsDao.findByMetricsCode(metricsCode);
        return metrics.stream().map(e -> {
            MetricsBaseResp baseResp = new MetricsBaseResp();
            baseResp.setMetricsCode(e.getNestCode());
            baseResp.setMetricsName(e.getNestName());
            return baseResp;
        }).collect(Collectors.toList());
    }

    private MetricsResp convert(DsMetricsByModule module){
        MetricsResp resp = new MetricsResp();
        resp.setMetricsType(module.getMetricType());
        resp.setMetricsCode(module.getCode());
        resp.setMetricsName(Optional.ofNullable(module.getAlias()).orElseGet(module::getName));
        return resp;
    }

}
