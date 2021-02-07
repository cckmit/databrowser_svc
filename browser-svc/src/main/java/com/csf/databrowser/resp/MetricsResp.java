package com.csf.databrowser.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author eric.yao
 * @date 2021/1/28
 **/
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MetricsResp extends MetricsBaseResp {

    private int metricsType;

}
