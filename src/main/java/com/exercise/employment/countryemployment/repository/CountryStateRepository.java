package com.exercise.employment.countryemployment.repository;

import com.exercise.employment.countryemployment.beans.CountryStateId;
import com.exercise.employment.countryemployment.beans.CountryStateMaster;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryStateRepository extends CrudRepository<CountryStateMaster, CountryStateId> {
}
