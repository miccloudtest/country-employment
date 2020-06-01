package com.exercise.employment.countryemployment.repository;

import com.exercise.employment.countryemployment.beans.State;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends CrudRepository<State, Integer> {
}
