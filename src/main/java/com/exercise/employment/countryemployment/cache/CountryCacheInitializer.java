package com.exercise.employment.countryemployment.cache;

import com.exercise.employment.countryemployment.beans.CountryStateIDMaster;
import com.exercise.employment.countryemployment.repositories.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CountryCacheInitializer {
    @Autowired
    private CountryRepository countryRepository;
    public Map<String, List<CountryStateIDMaster>> countryStateCache = new HashMap<>();

    @PostConstruct
    public void init() {
        List<CountryStateIDMaster> data = (List<CountryStateIDMaster>) countryRepository.loadCountryStateCache();
        countryStateCache =data.stream()
                .collect(Collectors.groupingBy(CountryStateIDMaster::getCountryName));
    }
}
