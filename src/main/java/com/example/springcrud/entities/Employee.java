package com.example.springcrud.entities;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.springcrud.utilities.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "employees")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Employee extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotNull(message = "Stipendio obbligatorio")
    @DecimalMin(value = "0.0", message = "Lo stipendio deve essere maggiore di 0")
    private Double salary;

    @Column(name = "hiring_date", nullable = false)
    @NotNull(message = "Data di assunzione obbligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate hiringDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Ruolo obbligatorio")
    private Role role;

    // FetchType.EAGER default carica immediatamente i vincoli (piu performante ma
    // con pochi dati)
    // FetchType.LAZY carica i vincoli solo quando richiesti (per moli di dati
    // elevate)
    // CascadeType.PERSIST le azioni di inserimento su emp si propagano a user
    // CascadeType.MERGE per aggiornamento
    // CascadeType.REMOVE eliminazione
    // CascadeType.refresh ricarica l'entita correlata
    @OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_employee_user"))
    @Valid
    private User user;

    @ManyToMany
    @JoinTable(name = "employees_projects", joinColumns = @JoinColumn(name = "employee_id"), inverseJoinColumns = @JoinColumn(name = "project_id"))
    private List<Project> projects;
}
