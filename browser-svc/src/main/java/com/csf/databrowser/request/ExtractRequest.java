package com.csf.databrowser.request;

import com.csf.databrowser.resp.MetricsResp;
import lombok.Data;

import java.util.List;

@Data
public class ExtractRequest {
   private List<String> csfIdList;
   private String groupCode;
   private List<MetricsResp> metrics;
}
