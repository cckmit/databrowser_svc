package com.csf.databrowser.resp;

import lombok.Data;

import java.util.List;

/**
 * @author eric.yao
 * @date 2021/1/28
 **/
@Data
public class ModuleMetricsResp {

    private String groupCode;

    private String groupName;

    private List<MetricsResp> metrics;

}
