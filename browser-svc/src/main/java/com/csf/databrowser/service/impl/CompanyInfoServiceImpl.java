package com.csf.databrowser.service.impl;

import com.csf.databrowser.resp.CompaniesESResp;
import com.csf.databrowser.service.CompanyInfoService;
import com.csf.databrowser.service.RestTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CompanyInfoServiceImpl implements CompanyInfoService {

    @Resource
    private RestTemplateService restTemplateService;

    @Value("${api_elastic_search_company_info}")
    private String api_elastic_search_company_info;

    @Override
    public CompaniesESResp getCompanyInfo(String keyword, Integer page,Integer size) {
        Map<String, Object> reqParams = new HashMap<>();
        reqParams.put("keyword", keyword);
        reqParams.put("page", page);
        reqParams.put("size", size);
        StopWatch watch = new StopWatch();
        watch.start();
        CompaniesESResp result = restTemplateService.getCompanyInfo(api_elastic_search_company_info, CompaniesESResp.class, reqParams);
        watch.stop();
        log.info("search company es keyword={}, takeTime={}ms", keyword, watch.getTotalTimeMillis());
        return result;
    }
}
