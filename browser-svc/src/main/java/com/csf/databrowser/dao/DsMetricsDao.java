package com.csf.databrowser.dao;

import com.csf.databrowser.entity.DsMetrics;
import com.csf.databrowser.entity.DsMetricsByModule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 指标表 Mapper 接口
 * </p>
 *
 * @author eric.yao
 * @since 2021-01-28
 */
@Mapper
public interface DsMetricsDao {

    /**
     * 通过moduleId查询指标信息
     * @param moduleId
     * @return
     */
    List<DsMetricsByModule> findMetricsAndGroupByModule(@Param("moduleId") Integer moduleId);

}
