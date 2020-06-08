package com.exercise.employment.countryemployment.beans;

import com.exercise.employment.countryemployment.annotations.ExcelColumn;
import com.exercise.employment.countryemployment.utils.serializers.CustomDoubleSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    private Long stateNumEmployed;
    @ExcelColumn(name = "Area")
    private Integer stateArea;
    @JsonSerialize(using = CustomDoubleSerializer.class)
    private Double employmentRate;
    @JsonIgnore
    private Integer countryId;
    @JsonIgnore
    private Integer stateId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss" ,locale = "en-IN")
    private Date created_ts;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss",locale = "en-IN")

    private Date modified_ts;
    private String created_by;
    private String modified_by;
}
