package com.csf.databrowser.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DsExportRecord {

    private String username;

    private String url;

    private Integer fileSize;

    private String csfIds;

    private String metrics;

    private Long duration;
}
