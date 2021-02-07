package com.csf.databrowser.service.impl;

import com.csf.databrowser.dao.CompanyScopeDao;
import com.csf.databrowser.dao.CompanyStdDao;
import com.csf.databrowser.extract.CompanyHelper;
import com.csf.databrowser.resp.CompanyExtractResp;
import com.csf.databrowser.resp.MetricsBaseResp;
import com.csf.databrowser.resp.MetricsResp;
import com.csf.databrowser.service.CompanyStdService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyStdServiceImpl implements CompanyStdService {

    @Resource
    private CompanyStdDao companyStdDao;
    @Resource
    private CompanyScopeDao companyScopeDao;

    @Override
    public CompanyExtractResp getCompanyExtract(String csfId, List<MetricsResp> metrics) {
        List<CompanyExtractResp> list = new ArrayList<>();
        CompanyExtractResp companyExtract = new CompanyExtractResp();
        companyExtract = companyStdDao.getCompanyExtract(csfId);
        List<String> nameList = metrics.stream().map(MetricsBaseResp::getMetricsCode).collect(Collectors.toList());
        //指标提取
        CompanyExtractResp resp = CompanyHelper.getCompany(nameList, CompanyExtractResp.class,companyExtract);
        return resp;
    }
}
