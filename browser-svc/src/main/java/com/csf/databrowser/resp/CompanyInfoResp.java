package com.csf.databrowser.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyInfoResp {
    private String csfId;
    private String name;
    private String secu;
    private String tick;
    private Integer listStatus;
}
