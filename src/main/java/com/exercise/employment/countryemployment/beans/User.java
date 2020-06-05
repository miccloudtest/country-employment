package com.exercise.employment.countryemployment.beans;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class User implements Serializable {
    private static final long serialVersionUID = -1890405266182054037L;
    private String userName;
    private String email;
}
