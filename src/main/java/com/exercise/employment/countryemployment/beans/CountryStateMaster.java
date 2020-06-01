package com.exercise.employment.countryemployment.beans;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "CountryStateMaster")
@ToString
@IdClass(CountryStateId.class)
public class CountryStateMaster {
    private static final long serialVersionUID = -7897700658518315266L;
    @Id
    private String countryName;
    @Id
    private String stateName;
}
