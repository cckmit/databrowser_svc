package com.csf.databrowser.service;

import com.csf.databrowser.resp.CompanyExtractResp;
import com.csf.databrowser.resp.MetricsResp;

import java.util.List;

public interface CompanyStdService {
    CompanyExtractResp getCompanyExtract(String csfId, List<MetricsResp> metrics);
}
