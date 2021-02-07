package com.csf.databrowser.service;

import com.csf.databrowser.resp.CompaniesESResp;

public interface CompanyInfoService {
    CompaniesESResp getCompanyInfo(String keyword,Integer page, Integer size);
}
