package com.exercise.employment.countryemployment.utils.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public abstract class CellParser {
    public static final Logger logger = LoggerFactory.getLogger(CellParser.class);

    public abstract void parse(Cell cell, Object object, Field field);


    protected void setField(Cell cell, Object object, Field field, Object cellValue) {
        try {
            // makeAccessible first
            field.set(object, cellValue);
        } catch (NumberFormatException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
