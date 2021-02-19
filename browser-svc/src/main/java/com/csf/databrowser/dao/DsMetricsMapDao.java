package com.csf.databrowser.dao;

import com.csf.databrowser.entity.DsMetricsMap;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 指标对应关系表 Mapper 接口
 * </p>
 *
 * @author eric.yao
 * @since 2021-01-28
 */
@Mapper
public interface DsMetricsMapDao {

    List<DsMetricsMap> getMetricsMap(@Param("list") Collection<String> metricCodes);

}
