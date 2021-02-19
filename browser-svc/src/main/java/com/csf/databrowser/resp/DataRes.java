package com.csf.databrowser.resp;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DataRes {

    private Integer total;
    private List<Map<String, Object>> data;

}
