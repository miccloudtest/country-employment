package com.exercise.employment.countryemployment.beans;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class CountryStateId implements Serializable {
    private static final long serialVersionUID = -7897700658518315266L;
    private String countryName;
    private String stateName;
}
