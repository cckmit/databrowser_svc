package com.csf.databrowser.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 指标表
 * </p>
 *
 * @author eric.yao
 * @since 2021-01-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DsMetrics implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

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

    private Integer status;

    private String createdBy;

    private LocalDateTime createdTime;

    private String updatedBy;

    private LocalDateTime updatedTime;


}
