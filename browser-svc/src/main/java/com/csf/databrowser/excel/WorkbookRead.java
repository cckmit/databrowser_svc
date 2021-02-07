package com.csf.databrowser.excel;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public abstract class WorkbookRead<T> {

    private InputStream inputStream;

    public WorkbookRead(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public T read(String sheetName) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = StringUtils.isNotBlank(sheetName)? workbook.getSheet(sheetName):
                    workbook.getSheetAt(0);
            if(sheet == null) {
                return null;
            }
            T result =  read(sheet);
            workbook.close();
            return result;
        }catch (IOException e) {
            log.error("read company file error", e);
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("close error", e);
                }
            }
        }
        return null;
    }

    abstract public T read(XSSFSheet sheet);

}
