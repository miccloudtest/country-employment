package com.exercise.employment.countryemployment.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.function.Function;
import java.util.function.Predicate;
@Component
public class ExcelUtil {
    static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    public static Predicate<MultipartFile> isXls = file -> file.getOriginalFilename().toLowerCase().endsWith("xls");

    public static Predicate<MultipartFile> isXlsx = file -> file.getOriginalFilename().toLowerCase().endsWith("xlsx");

    public static Function<MultipartFile, Workbook> getWorkBook = file -> {
        Workbook workbook = null;
        try {
            if (isXlsx.test(file)) {
                workbook = new XSSFWorkbook(file.getInputStream());
            } else if (isXls.test(file)) {
                workbook = new HSSFWorkbook(file.getInputStream());
            }
        } catch (Exception ex) {
            logger.error("The specified file is not Excel file");
            throw new IllegalArgumentException("The specified file is not Excel file");
        }
        return workbook;
    };
}
