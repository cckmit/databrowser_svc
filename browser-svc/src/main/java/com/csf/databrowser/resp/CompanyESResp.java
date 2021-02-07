package com.csf.databrowser.resp;

import lombok.Data;

import java.util.List;

@Data
public class CompanyESResp {
    private String csfId;
    private String name;
    private List<Security> securities;
    private List<String> bondMkts;

    @Data
    public static class Security {
        private String secu;
        private String tick;
        private String mkt;
        private String ls;
    }
}
