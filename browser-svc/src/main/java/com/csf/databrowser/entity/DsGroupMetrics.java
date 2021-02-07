package com.csf.databrowser.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 指标与指标关系表
 * </p>
 *
 * @author eric.yao
 * @since 2021-01-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DsGroupMetrics implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 指标组code
     */
    private String groupCode;

    /**
     * 指标code
     */
    private String metricCode;

    private Integer status;

    private String createdBy;

    private LocalDateTime createdTime;

    private String updatedBy;

    private LocalDateTime updatedTime;


}
