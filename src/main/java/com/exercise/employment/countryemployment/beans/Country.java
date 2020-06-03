package com.exercise.employment.countryemployment.beans;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "states")
@Table(name = "country")
public class Country implements Serializable {
    @Id
    private String country;
    @OneToMany(cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY,
            mappedBy = "country")
    private Set<State> states;

}
