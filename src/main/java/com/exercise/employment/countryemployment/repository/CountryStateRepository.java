package com.exercise.employment.countryemployment.repository;

import com.exercise.employment.countryemployment.beans.CountryStateMapping;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryStateRepository extends CrudRepository<CountryStateMapping, Integer> {
}
