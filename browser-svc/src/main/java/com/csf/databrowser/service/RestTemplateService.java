package com.csf.databrowser.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class RestTemplateService {

    @Autowired
    private RestTemplate restTemplate;

    public <T> T getCompanyInfo(String url, Class<T> type, Map<String, ?> params) {
        return restTemplate.getForObject(url, type, params);
    }
}
