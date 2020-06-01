package com.exercise.employment.countryemployment.Utils;

import com.exercise.employment.countryemployment.beans.CountryStateMaster;
import com.exercise.employment.countryemployment.repository.CountryStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class Util {
    @Autowired
    public CountryStateRepository countryStateRepository;
    public static Map<String, Set<String>> countryStatePairMap = new HashMap<>();

    @PostConstruct
    public void init() {
        System.out.println("ghgjhgfjdgfjhgdsfhgjhsdfgjhsgdfhgsdj");
        List<CountryStateMaster> data = (List<CountryStateMaster>) countryStateRepository.findAll();
        System.out.println(data);
        data.forEach(countryStateMaster -> {
            if (!countryStatePairMap.containsKey(countryStateMaster.getCountryName())) {

                Set<String> state = new HashSet<>();
                state.add(countryStateMaster.getStateName());
                countryStatePairMap.put(countryStateMaster.getCountryName(), state);
                System.out.println(countryStateMaster);
            } else {
                countryStatePairMap.get(countryStateMaster.getCountryName()).add(countryStateMaster.getStateName());
            }

        });

    }
}
