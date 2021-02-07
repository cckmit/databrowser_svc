package com.csf.databrowser.resp;

import lombok.Data;

import java.util.List;

@Data
public class CompaniesInfoResp {
    private List<CompanyInfoResp> result;
}
