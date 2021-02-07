package com.csf.databrowser.resp;


import lombok.Data;

import java.util.List;

@Data
public class CompanySearchResp<T> {

    private List<T> result;
}
