package com.exercise.employment.countryemployment.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
public class ExcelRecord {
    @JsonIgnore
    private List<CountryData> validRecords;
    private List<CountryData> inValidRecords;
    private List<CountryData> insertRecords;
    private List<CountryData> updateRecords;
}
