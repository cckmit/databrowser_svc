package com.csf.databrowser.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class SheetBuilder {
    protected SheetTemplate template;
    protected Workbook workbook;
    protected Sheet sheet;
    protected int currentRow;


    public SheetBuilder(SheetTemplate template, Workbook workbook) {
        this.template = template;
        this.workbook = workbook;
        this.sheet = workbook.createSheet(template.getSheetName());
    }

    protected void writeHeader() {
        Row headerRow = sheet.createRow(currentRow++);
        int colIndex = 0;
        for (String header : template.getHeaders()) {
            writeCell(header, headerRow.createCell(colIndex++));
        }
    }

    protected void writeContent() {
        List<List<Object>> contentList = template.getContent();
        for (List<Object> list : contentList) {
            int columnIndex = 0;
            Row row = sheet.createRow(currentRow++);
            for (Object value : list) {
                writeCell(value, row.createCell(columnIndex++));
            }
        }
    }

    protected void writeCell(Object value, Cell cell) {

        if (value instanceof String) {
            cell.setCellValue(value.toString());
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue((LocalDateTime) value);
        } else if (value instanceof LocalDate) {
            cell.setCellValue((LocalDate) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        } else if (value instanceof Byte) {
            cell.setCellValue((Byte) value);
        } else if (value instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal)value).toPlainString());
        } else if (value instanceof BigInteger) {
            cell.setCellValue(((BigInteger)value).longValue());
        }

    }

    public Sheet build() {
        writeHeader();
        writeContent();
        return sheet;

    }
}
