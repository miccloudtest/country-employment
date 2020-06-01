package com.exercise.employment.countryemployment.beans;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "states")
@ToString
public class State implements Serializable {
    private static final long serialVersionUID = -8216899407588441009L;
    @Id
    @Column(name = "state_name")
    private String stateName;
    @Column(name = "population")
    private Long population;
    @Column(name = "No_Of_Employed")
    private Long noOfEmployed;
    private Integer area;
    private String isDeleted;
    @Column(name = "last_updated_ts")
    @Temporal(TemporalType.DATE)
    private Date lastUpdatedTs;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;
        State state = (State) o;
        return population == state.population &&
                noOfEmployed == state.noOfEmployed &&
                Double.compare(state.employmentRate, employmentRate) == 0 &&
                Objects.equals(stateName, state.stateName) &&
                Objects.equals(area, state.area) &&
                Objects.equals(isDeleted, state.isDeleted) &&

                Objects.equals(country, state.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateName, population, noOfEmployed, area, isDeleted, employmentRate, country);
    }

    @Column(name = "Employment_Rate")
    private Double employmentRate;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "country", nullable = false)
    @JsonIgnore
    private Country country;
    @Transient
    @JsonAlias("country")
    private String associatedCountry;
}
