package com.example.springcrud.entities;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@MappedSuperclass
@Data
public class Person {

    @Column(nullable = false)
    @NotBlank(message = "Nome obbligatorio")
    @Size(min = 3, max = 30, message = "Deve contenere tra 3 e 30 caratteri")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Cognome obbligatorio")
    @Size(min = 3, max = 30, message = "Deve contenere tra 3 e 30 caratteri")
    private String surname;

    @NotBlank(message = "Codice Fiscale obbligatorio")
    @Pattern(regexp = "^[A-Z]{6}\\d{2}[A-EHLMPR-T][\\d]{2}[A-Z][\\d]{3}[A-Z]$", message = "Codice fiscale non valido")
    @Column(name = "fiscal_code", nullable = false, unique = true)
    private String fiscalCode;

    @Column(name = "birth_date", nullable = false)
    @NotNull(message = "Data di nascita obbligatoria")
    // @DateTimeFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

}
