package com.csf.databrowser.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 指标对应关系表
 * </p>
 *
 * @author eric.yao
 * @since 2021-01-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DsMetricsMap implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 指标code
     */
    private String metricCode;

    /**
     * 嵌套code
     */
    private String nestCode;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 列名
     */
    private String columnName;

    /**
     * 额外信息
     */
    private String extraInfo;

    private Integer status;

    private String createdBy;

    private LocalDateTime createdTime;

    private String updatedBy;

    private LocalDateTime updatedTime;


}
