package com.exercise.employment.countryemployment.repository;

import com.exercise.employment.countryemployment.beans.CountryStateMapping;
import com.exercise.employment.countryemployment.beans.ExcelSheet;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IExcelRepository {
    List<CountryStateMapping> loadCountryStateCache();
    public Map<Integer, Integer> getExistingCountryStateId(Set<Integer> states);
    void insertBatchExecute(List<ExcelSheet> excelRecord) throws Exception;
    void updateBatchExecute(List<ExcelSheet> excelRecord);



}
