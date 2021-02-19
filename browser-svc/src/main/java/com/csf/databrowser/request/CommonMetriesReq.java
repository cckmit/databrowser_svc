package com.csf.databrowser.request;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.csf.risk.common.util.RiskUtil.isIllegalException;

@Data
public class CommonMetriesReq {

    private Integer page = 0;

    private Integer size = 20;

    private List<String> csfIds;

    private List<MetricsInfo> metricsInfos;

    @Data
    public static  class MetricsInfo {
        //标题
        private String title;
        //指标编码
        private String metricsCode;
        //输出类型
        private OutputInfo outputInfo;

        public String getKey() {
          StringBuilder sb = new StringBuilder(metricsCode);
          if (outputInfo != null) {
              sb.append("," + outputInfo.getType());
              sb.append("," + outputInfo.getValue());
          }
          return sb.toString();
        }
    }

    @Data
    public static class OutputInfo {
        //类型
        private String type;
        //类型值
        private String value;
    }

    public void parameterCheck(boolean includeTitle) {
        isIllegalException(CollectionUtils.isEmpty(csfIds) || CollectionUtils.isEmpty(metricsInfos), "csfids or metricsInfos");
        metricsInfos.forEach(x -> {
            isIllegalException(StringUtils.isBlank(x.getMetricsCode()), "metricsCode");
            if (includeTitle) {
                isIllegalException(StringUtils.isBlank(x.getTitle()), "title");
            }
        });
    }

}
