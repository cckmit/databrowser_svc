package com.csf.databrowser.controller;

import com.csf.databrowser.resp.MetricsBaseResp;
import com.csf.databrowser.resp.ModuleMetricsResp;
import com.csf.databrowser.service.MetricsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author eric.yao
 * @date 2021/1/28
 **/
@RestController
@RequestMapping("/metrics")
public class MetricsController {

    @Resource
    private MetricsService metricsService;

    @GetMapping
    public ResponseEntity<List<ModuleMetricsResp>> findModuleMetrics(@RequestParam("moduleId") Integer moduleId){
        return new ResponseEntity<>(metricsService.findModuleMetrics(moduleId), HttpStatus.OK);
    }

    @GetMapping("combine")
    public ResponseEntity<List<MetricsBaseResp>> findCombineMetrics(@RequestParam("metricsCode") String metricsCode){
        return new ResponseEntity<>(metricsService.findCombineMetrics(metricsCode),HttpStatus.OK);
    }
}
