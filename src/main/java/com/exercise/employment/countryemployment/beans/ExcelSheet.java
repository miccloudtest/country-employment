package com.exercise.employment.countryemployment.beans;

import com.exercise.employment.countryemployment.annotations.ExcelColumn;
import lombok.*;

import java.io.Serializable;
import java.util.Date;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExcelSheet implements Serializable {
    private static final long serialVersionUID = -5136679304429296791L;
    @ExcelColumn(name = "Country")
    private String countryName;
    @ExcelColumn(name = "State")
    private String stateName;
    @ExcelColumn(name = "Population")
    private Long population;
    @ExcelColumn(name = "NoOfEmployed")
    private Long noOfEmployed;
    @ExcelColumn(name = "Area")
    private Integer area;
    @ExcelColumn(name = "isDeleted")
    private String isDeleted;
    @ExcelColumn(name = "Date")
    private Date lastUpdatedTs;
    private Double employmentRate;


}
