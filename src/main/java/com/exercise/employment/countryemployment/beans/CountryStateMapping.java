package com.exercise.employment.countryemployment.beans;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "CountryStateMaster")
@ToString

public class CountryStateMapping {
    private static final long serialVersionUID = -7897700658518315266L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CTM_SEQ")
    @SequenceGenerator(sequenceName = "country_state_map_seq", allocationSize = 1, name = "CTM_SEQ")
    private Integer id;
    private String countryName;
    @Column(unique = true)
    private String stateName;
}
