package com.csf.databrowser.service;

import com.csf.databrowser.request.CommonMetriesReq;
import com.csf.databrowser.request.ExtractRequest;
import com.csf.databrowser.resp.CompaniesExtractResp;
import com.csf.databrowser.resp.CompaniesInfoResp;
import com.csf.databrowser.resp.CompanySearchResp;
import com.csf.databrowser.resp.DataRes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

public interface CompanyService {

    CompaniesInfoResp getCompanyInfo(String keyword, Integer page, Integer size);

    CompanySearchResp searchCompanys(InputStream is);

    CompaniesExtractResp extract(ExtractRequest request);

    String download(HttpServletRequest request);

    DataRes getPageData(CommonMetriesReq req);

    void exportCompanyInfo(CommonMetriesReq req, HttpServletResponse response, HttpServletRequest request);
}

