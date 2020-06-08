package com.exercise.employment.countryemployment.beans;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
public class ExcelRecord {
    private List<CountryData> validRecords;
    private List<CountryData> inValidRecords;
    private List<CountryData> insertRecords;
    private List<CountryData> updateRecords;
}
