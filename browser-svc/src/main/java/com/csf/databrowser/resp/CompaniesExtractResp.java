package com.csf.databrowser.resp;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Setter
@Getter
public class CompaniesExtractResp implements Cloneable{
    private List<CompanyExtractResp> extractRespList;

    @Override
    public CompaniesExtractResp clone() throws CloneNotSupportedException {
        CompaniesExtractResp resp = (CompaniesExtractResp)super.clone();
        resp.setExtractRespList(
            Optional.ofNullable(extractRespList).orElseGet(ArrayList::new).stream().map(CompanyExtractResp::clone).collect(Collectors.toList())
        );
        return resp;
    }
}
