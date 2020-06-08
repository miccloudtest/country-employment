package com.exercise.employment.countryemployment.annotations;
import com.exercise.employment.countryemployment.utils.excel.CellParser;
import com.exercise.employment.countryemployment.utils.excel.DefaultCellParser;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target(FIELD)
@Retention(RUNTIME)
public @interface ExcelColumn {
    String name() ;
    Class<? extends CellParser> cellParser() default DefaultCellParser.class;
}

