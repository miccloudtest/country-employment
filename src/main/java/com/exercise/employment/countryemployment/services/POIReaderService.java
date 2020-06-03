package com.exercise.employment.countryemployment.services;

import com.exercise.employment.countryemployment.annotations.ExcelColumn;
import com.exercise.employment.countryemployment.beans.ExcelSheetDescriptor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class POIReaderService<R> {
    Logger logger = LoggerFactory.getLogger(POIReaderService.class);

    public List<R> readFile(InputStream inputStream, Class<R> bean) throws Exception {
        List<R> rows = null;
        try {
            ExcelSheetDescriptor<R> sheetDescriptor = new ExcelSheetDescriptor<>(bean);
            rows = readSheet(inputStream,sheetDescriptor );
        } catch (InstantiationException | IllegalAccessException | IOException e) {
            logger.error("Error in file read operation {}", e.getMessage());
            throw e;
        }
        return rows;

    }

    private <R> List<R> readSheet(InputStream fileStream, ExcelSheetDescriptor<R> sheetDescriptor) throws IOException, InstantiationException, IllegalAccessException {
        List<R> excelRecords = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(fileStream)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIt = sheet.iterator();
            int currentRowIndex = 0;
            while (rowIt.hasNext()) {
                Row row = rowIt.next();
                if (currentRowIndex == 0) {
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        if (cell == null || cell.getCellType() == CellType.BLANK) {
                            break;
                        }
                        sheetDescriptor.getExcelHeaderMap().put(cell.getColumnIndex(), cell.getStringCellValue().trim());
                    }
                    currentRowIndex++;
                    continue;
                }
                R rowInstance = sheetDescriptor.getRowClass().newInstance();
                // Iterate over row cells
                Field fieldDef;
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    fieldDef = sheetDescriptor.getFieldsMapper().get(sheetDescriptor.getExcelHeaderMap().get(cell.getAddress().getColumn()));
                    if (fieldDef != null)
                        fieldDef.getAnnotation(ExcelColumn.class).cellParser().newInstance().parse(cell, rowInstance, fieldDef);
                }
                excelRecords.add(rowInstance);
                currentRowIndex++;
            }
        }
        return excelRecords;
    }

}
