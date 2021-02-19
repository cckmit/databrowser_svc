package com.csf.databrowser.dao;


import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DsPublicDao {

    List<Map<String, Object>> selectPublicItemList(@Param(value = "sqlStr") String sqlStr);

}
