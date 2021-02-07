package com.csf.databrowser.entity;

import lombok.Data;

/**
 * @author eric.yao
 * @date 2021/1/28
 **/
@Data
public class DsMetricsByModule {

    private Integer moduleId;

    /**
     * 指标组code
     */
    private String groupCode;

    /**
     * 指标组名称
     */
    private String groupName;

    private String code;

    private String name;

    /**
     * 别名
     */
    private String alias;

    /**
     * 1:单一指标 2：单一指标，上市与非上市之分 3：复合指标：有参数 4:复合指标：子指标
     */
    private Integer metricType;

}
