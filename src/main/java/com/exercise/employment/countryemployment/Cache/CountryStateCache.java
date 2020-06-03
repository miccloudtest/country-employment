package com.exercise.employment.countryemployment.Cache;

import com.exercise.employment.countryemployment.beans.CountryStateMapping;
import com.exercise.employment.countryemployment.repository.CountryStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class CountryStateCache {
    @Autowired
    private CountryStateRepository countryStateRepository;
    public Map<String, Set<String>> countryStateMap = new HashMap<>();

    @PostConstruct
    public void init() {
        List<CountryStateMapping> data = (List<CountryStateMapping>) countryStateRepository.findAll();
        data.forEach(countryStateMaster -> {
            if (!countryStateMap.containsKey(countryStateMaster.getCountryName())) {
                Set<String> state = new HashSet<>();
                state.add(countryStateMaster.getStateName());
                countryStateMap.put(countryStateMaster.getCountryName(), state);
            } else {
                countryStateMap.get(countryStateMaster.getCountryName()).add(countryStateMaster.getStateName());
            }
        });

    }
}
