package com.csf.databrowser.excel;

import java.util.List;

public interface SheetTemplate {
    String getSheetName();

    String[] getHeaders();

    List<List<Object>> getContent();

}
