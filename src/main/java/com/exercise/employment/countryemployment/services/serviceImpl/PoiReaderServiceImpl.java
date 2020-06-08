package com.exercise.employment.countryemployment.services.serviceImpl;

import com.exercise.employment.countryemployment.annotations.ExcelColumn;
import com.exercise.employment.countryemployment.beans.ExcelSheetDescriptor;
import com.exercise.employment.countryemployment.services.service.PoiReaderService;
import com.exercise.employment.countryemployment.utils.excel.ExcelUtil;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class PoiReaderServiceImpl<R> implements PoiReaderService {
    Logger logger = LoggerFactory.getLogger(PoiReaderServiceImpl.class);


    @Override
    public List readFile(MultipartFile file, Class bean) throws Exception {
        List<R> rows = null;
        try {
            ExcelSheetDescriptor<R> sheetDescriptor = new ExcelSheetDescriptor<>(bean);
            rows = readSheet(file, sheetDescriptor);
        } catch (InstantiationException | IllegalAccessException | IOException e) {
            logger.error("Error in file read operation {}", e.getMessage());
            throw e;
        }
        return rows;
    }

    private <R> List<R> readSheet(MultipartFile file, ExcelSheetDescriptor<R> sheetDescriptor) throws IOException, InstantiationException, IllegalAccessException {
        List<R> excelRecords = new ArrayList<>();
        try (Workbook workbook = ExcelUtil.getWorkBook.apply(file)) {
            Sheet sheet = workbook.getSheetAt(0);
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
