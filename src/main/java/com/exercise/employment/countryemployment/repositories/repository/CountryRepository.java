package com.exercise.employment.countryemployment.repositories.repository;

import com.exercise.employment.countryemployment.beans.CountryStateIDMaster;
import com.exercise.employment.countryemployment.beans.CountryData;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CountryRepository {
    List<CountryStateIDMaster> loadCountryStateCache();

    public Map<Integer, Integer> getExistingCountryStateId(Set<Integer> states);

    void insertBatchExecute(List<CountryData> excelRecord) throws Exception;

    void updateBatchExecute(List<CountryData> excelRecord);

    List<CountryData> getCountriesData();

    void exceCountryStateBackUpProc();
}
