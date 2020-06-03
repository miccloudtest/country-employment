package com.exercise.employment.countryemployment.beans;

import com.exercise.employment.countryemployment.annotations.ExcelColumn;
import com.exercise.employment.countryemployment.utils.reflection.ReflectionUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ExcelSheetDescriptor<R> {

    private Class<R> rowClass;
    Map<String, Field> fieldsMapper;
    @Getter
    @Setter
    Map<Integer, String> excelHeaderMap = new HashMap<>();

    public ExcelSheetDescriptor(Class<R> rowClass) {
        this.rowClass = rowClass;
        fieldsMapper = new HashMap<>();
        ReflectionUtils.executeOnClassFieldsByAnnotation(rowClass, ExcelColumn.class, fieldDef -> {
            ExcelColumn cellInfo = fieldDef.getAnnotation(ExcelColumn.class);
            fieldsMapper.put(cellInfo.name(), fieldDef);
        });
    }
}
