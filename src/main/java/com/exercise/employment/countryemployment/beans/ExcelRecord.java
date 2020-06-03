package com.exercise.employment.countryemployment.beans;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
public class ExcelRecord {
    private List<ExcelSheet> validRecords;
    private List<ExcelSheet> inValidRecords;
}
