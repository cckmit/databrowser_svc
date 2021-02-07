package com.csf.databrowser.dao;

import com.csf.databrowser.resp.CompanyExtractResp;

public interface CompanyStdDao {

    CompanyExtractResp getCompanyExtract(String csfId);
}
