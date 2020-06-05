package com.exercise.employment.countryemployment.Cache;

import com.exercise.employment.countryemployment.beans.CountryStateMapping;
import com.exercise.employment.countryemployment.repository.IExcelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CountryStateCache {
    @Autowired
    private IExcelRepository excelRepository;
    public Map<String, List<CountryStateMapping>> countryStateMap = new HashMap<>();

    @PostConstruct
    public void init() {
        List<CountryStateMapping> data = (List<CountryStateMapping>) excelRepository.loadCountryStateCache();
        countryStateMap =data.stream()
                .collect(Collectors.groupingBy(CountryStateMapping::getCountryName));
    }
}
