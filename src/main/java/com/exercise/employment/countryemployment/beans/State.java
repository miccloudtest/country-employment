package com.exercise.employment.countryemployment.beans;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "state")
public class State implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STT_SEQ")
    @SequenceGenerator(sequenceName = "state_seq", allocationSize = 1, name = "STT_SEQ")
    private Integer id;

    @Column(name = "state_name")
    private String stateName;
    @Column(name = "population")
    private Long population;
    @Column(name = "No_Of_Employed")
    private Long noOfEmployed;
    private Integer area;
    private String isDeleted;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "country", nullable = false)
    private Country country;
    @Column(name = "last_updated_ts")
    @Temporal(TemporalType.DATE)
    private Date lastUpdatedTs;


}
