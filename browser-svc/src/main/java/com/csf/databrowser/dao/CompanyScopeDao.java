package com.csf.databrowser.dao;

import com.csf.databrowser.resp.CompanyExtractResp;

public interface CompanyScopeDao {
    CompanyExtractResp getCompanyExtract(String csfId);
}
