package com.exercise.employment.countryemployment.beans;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

@Data
@Builder
@EqualsAndHashCode
public class ExcelRecord {
    private List<Country> validRecords;
    private Set<State> invalidStateSet;
}
