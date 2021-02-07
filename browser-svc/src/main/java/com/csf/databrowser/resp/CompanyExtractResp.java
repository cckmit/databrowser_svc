package com.csf.databrowser.resp;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyExtractResp implements Cloneable {

    public CompanyExtractResp() {
    }
    @Excel(name = "企业中文名称",width = 20,isColumnHidden = true)
    private String stdName;
    @Excel(name = "统一社会信用代码",width = 20,isColumnHidden = true)
    private String creditCode;
    @Excel(name = "注册资本",width = 20,isColumnHidden = true)
    private String regCapital;
    @Excel(name = "成立日期",width = 20,isColumnHidden = true)
    private String estiblishTime;
    @Excel(name = "存续状态",width = 20,isColumnHidden = true)
    private String regStatus;
    @Excel(name = "法定代表人",width = 20,isColumnHidden = true)
    private String legalPersonName;
    @Excel(name = "企业类型",width = 20,isColumnHidden = true)
    private String companyType;
    @Excel(name = "营业期限开始日期",width = 20,isColumnHidden = true)
    private String fromTime;
    @Excel(name = "营业期限终止日期",width = 20,isColumnHidden = true)
    private String toTime;
    @Excel(name = "注册地址",width = 20,isColumnHidden = true)
    private String regLocation;
    @Excel(name = "办公地址",width = 20,isColumnHidden = true)
    private String postalAddress;
    @Excel(name = "经营范围",width = 20,isColumnHidden = true)
    private String businessScope;
    @Excel(name = "邮编",width = 20,isColumnHidden = true)
    private String postcode;
    @Excel(name = "公司电话",width = 20,isColumnHidden = true)
    private String phoneNumber;
    @Excel(name = "公司电子邮件地址",width = 20,isColumnHidden = true)
    private String companyPostcode;
    @Excel(name = "公司网站",width = 20,isColumnHidden = true)
    private String website;
    @Excel(name = "是否上市",width = 20,isColumnHidden = true)
    private String listStatus;
    @Excel(name = "股票简称",width = 20,isColumnHidden = true)
    private String ticker;
    @Excel(name = "股票代码",width = 20,isColumnHidden = true)
    private String abbr;
    @Excel(name = "上市交易所",width = 20,isColumnHidden = true)
    private String mkt;
    @Excel(name = "首发上市日期",width = 20,isColumnHidden = true)
    private String listDt;
    @Excel(name = "摘牌日期",width = 20,isColumnHidden = true)
    private String listEdt;

    @Override
    public CompanyExtractResp clone() {
        try {
            return (CompanyExtractResp)super.clone();
        } catch (CloneNotSupportedException ignore) {

        }
        return null;
    }
}
