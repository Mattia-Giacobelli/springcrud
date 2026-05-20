package com.example.springcrud.utilities;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Component;

@Component
public class AgeUtility {

    public int getAge(LocalDate birthDate) {

        LocalDate today = LocalDate.now();

        if (birthDate != null) {

            return Period.between(birthDate, today).getYears();

        } else {

            return 0;

        }

    }

}
