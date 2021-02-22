package com.csf.databrowser.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class WorkbookBuilder {
    protected OutputStream outputStream;
    protected List<SheetTemplate> sheetTemplateList;

    public WorkbookBuilder(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void addSheet(SheetTemplate sheetTemplate) {
        if (sheetTemplateList == null) {
            sheetTemplateList = new ArrayList<>();
        }
        sheetTemplateList.add(sheetTemplate);
    }

    public int build() throws IOException {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        for (SheetTemplate sheetTemplate : sheetTemplateList) {
            new SheetBuilder(sheetTemplate, workbook).build();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        int size = byteArrayOutputStream.size();
        outputStream.write(byteArrayOutputStream.toByteArray());
        byteArrayOutputStream.close();
        outputStream.close();
        workbook.close();
        if (!workbook.dispose()) {
            log.warn("delete excel temporary files fail");
        }
        return size;
    }
}
