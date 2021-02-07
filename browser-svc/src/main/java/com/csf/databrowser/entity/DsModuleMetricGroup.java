package com.csf.databrowser.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 模块与指标组关系表
 * </p>
 *
 * @author eric.yao
 * @since 2021-01-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DsModuleMetricGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 模块ID
     */
    private Integer moduleId;

    /**
     * 指标组code
     */
    private String groupCode;

    private Integer status;

    private String createdBy;

    private LocalDateTime createdTime;

    private String updatedBy;

    private LocalDateTime updatedTime;


}
