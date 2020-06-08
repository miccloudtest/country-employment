package com.exercise.employment.countryemployment.beans;

import com.exercise.employment.countryemployment.annotations.ExcelColumn;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.util.Date;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountryData<R> implements Serializable {
    private static final long serialVersionUID = -5136679304429296791L;
    @ExcelColumn(name = "Country")
    private String countryName;
    @ExcelColumn(name = "State")
    private String stateName;
    @ExcelColumn(name = "Population")
    private Long statePopulation;
    @ExcelColumn(name = "NoOfEmployed")
    private Long stateNumOfEmployed;
    @ExcelColumn(name = "Area")
    private Integer stateArea;
    @JsonIgnore
    private Double employmentRate;
    @JsonIgnore
    private Integer countryId;
    @JsonIgnore
    private Integer stateId;
    @JsonIgnore
    private Date created_ts;
    @JsonIgnore
    private Date modified_ts;
    @JsonIgnore
    private String created_by;
    @JsonIgnore
    private String modified_by;
}
