package com.csf.databrowser.service;

import com.csf.databrowser.resp.MetricsBaseResp;
import com.csf.databrowser.resp.ModuleMetricsResp;

import java.util.List;

/**
 * @author eric.yao
 * @date 2021/1/28
 **/
public interface MetricsService {

    /**
     * 查询模块指标信息
     * @param moduleId
     * @return
     */
    public List<ModuleMetricsResp> findModuleMetrics(Integer moduleId);

    /**
     * 查询复合指标
     * @param metricsCode
     * @return
     */
    public List<MetricsBaseResp> findCombineMetrics(String metricsCode);

}
