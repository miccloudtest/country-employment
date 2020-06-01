package com.exercise.employment.countryemployment.beans;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "country")
@Builder
public class Country implements Serializable {

    private static final long serialVersionUID = -3135212873601832992L;
    @Id
    @Column(name = "country")
    private String countryName;
    @OneToMany(cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY,
            mappedBy = "country")
    private Set<State> states = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country)) return false;
        Country country = (Country) o;
        return Objects.equals(countryName, country.countryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryName);
    }
}
