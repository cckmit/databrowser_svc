package com.csf.databrowser.convert;


import com.csf.databrowser.request.CommonMetriesReq;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

@Service
public class RegCapitalWanValueConvert implements ValueConvert {

    @Override
    public boolean match(CommonMetriesReq.MetricsInfo metricsInfo) {
        if (metricsInfo == null || metricsInfo.getOutputInfo() == null) {
            return false;
        }

        if (!Objects.equals(metricsInfo.getOutputInfo().getType(), "CCY")) {
            return false;
        }

        if (!Objects.equals(metricsInfo.getOutputInfo().getValue(), "W")) {
            return false;
        }

        return true;
    }

    @Override
    public Object transfer(Object source) {
        if (source == null) {
            return null;
        }

        int value = 10000;
        if (source instanceof BigDecimal) {
            return ((BigDecimal) source).divide(BigDecimal.valueOf(value));
        } else if (source instanceof BigInteger) {
            return ((BigInteger) source).divide(BigInteger.valueOf(value));
        }else if (source instanceof Double) {
            return ((Double) source) / value;
        } else if (source instanceof Float) {
            return ((Float) source) / value;
        } else if (source instanceof Long) {
            return ((Long) source) / value;
        } else if (source instanceof Integer) {
            return ((Integer) source) / value;
        } else if (source instanceof  Short) {
            return ((Short) source) / value;
        }
        //不符合，不进行转化，返回原值
        return source;
    }

}
