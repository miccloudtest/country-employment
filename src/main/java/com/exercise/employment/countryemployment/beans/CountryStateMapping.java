package com.exercise.employment.countryemployment.beans;

import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryStateMapping implements Serializable {
    private static final long serialVersionUID = -7897700658518315266L;
    private Integer countryId;
    private String countryName;
    private Integer stateId;
    private String stateName;
}
