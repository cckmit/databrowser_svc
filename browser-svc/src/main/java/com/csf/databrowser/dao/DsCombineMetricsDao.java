package com.csf.databrowser.dao;

import com.csf.databrowser.entity.DsCombineMetrics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 组合指标表 Mapper 接口
 * </p>
 *
 * @author eric.yao
 * @since 2021-01-28
 */
public interface DsCombineMetricsDao {

    /**
     * 根据指标code查询复合指标信息
     * @param metricsCode
     * @return
     */
    List<DsCombineMetrics> findByMetricsCode(@Param("metricsCode") String metricsCode);

}
