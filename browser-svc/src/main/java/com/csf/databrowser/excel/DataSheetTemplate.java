package com.csf.databrowser.excel;

import java.util.List;

public class DataSheetTemplate implements SheetTemplate {
    private String sheetName;
    private String[] headers;
    private List<List<Object>> contentList;

    public DataSheetTemplate(List<List<Object>> contentList, String[] headers, String sheetName) {
        this.contentList = contentList;
        this.headers = headers;
        this.sheetName = sheetName;
    }

    @Override
    public String getSheetName() {
        return sheetName;
    }

    @Override
    public String[] getHeaders() {
        return headers;
    }

    @Override
    public List<List<Object>> getContent() {
        return contentList;
    }
}
