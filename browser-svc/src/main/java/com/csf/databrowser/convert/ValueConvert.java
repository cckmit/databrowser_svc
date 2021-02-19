package com.csf.databrowser.convert;


import com.csf.databrowser.request.CommonMetriesReq;

public interface ValueConvert {

    boolean match(CommonMetriesReq.MetricsInfo metricsInfo);

    Object transfer(Object source);

}
