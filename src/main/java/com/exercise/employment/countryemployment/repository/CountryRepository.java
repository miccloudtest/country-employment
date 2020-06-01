package com.exercise.employment.countryemployment.repository;

import com.exercise.employment.countryemployment.beans.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends CrudRepository<Country, Integer> {
}


