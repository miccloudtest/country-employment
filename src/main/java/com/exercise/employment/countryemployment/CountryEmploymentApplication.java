package com.exercise.employment.countryemployment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EntityScan("com.exercise.employment.countryemployment.beans")
@PropertySource("classpath:application.properties")
public class CountryEmploymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(CountryEmploymentApplication.class, args);
    }

}
