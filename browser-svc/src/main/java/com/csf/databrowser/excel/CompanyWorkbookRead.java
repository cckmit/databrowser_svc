package com.csf.databrowser.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompanyWorkbookRead extends WorkbookRead<List<String>> {

    public CompanyWorkbookRead(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    public List<String> read(XSSFSheet sheet) {
        List<String> result = new ArrayList<>();
        Set<String> companyNames = new HashSet<>(sheet.getLastRowNum());
        //skip title
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            XSSFRow xssfRow = sheet.getRow(rowIndex);
            if (xssfRow == null) {
                continue;
            }

            XSSFCell xssfCell = xssfRow.getCell(0);
            if (xssfCell.getCellType() == CellType.STRING) {
                String companyName = xssfCell.getStringCellValue();
                if (StringUtils.isNotBlank(companyName) && companyNames.add(companyName)) {
                    result.add(companyName);
                }
            }
        }
        return result;
    }
}
