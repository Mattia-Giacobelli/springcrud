package com.example.springcrud.entities;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "projects")
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "campo obbligatorio")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "campo obbligatorio")
    @DecimalMin(value = "0.0", message = "Lo stipendio deve essere maggiore di 0")
    private Double budget;

    @Column(name = "client_name", nullable = false)
    @NotBlank(message = "campo obbligatorio")
    private String clientName;

    @Column(nullable = false)
    @NotNull(message = "campo obbligatorio")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(nullable = false)
    @NotNull(message = "campo obbligatorio")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @AssertTrue(message = "La data di fine deve essere successiva alla data di inizio")
    @JsonIgnore
    public boolean isEndDateAfterStartDate() {
        if (this.startDate == null || this.endDate == null) {
            return true;
        }

        return endDate.isAfter(startDate);

    }

    @ManyToMany(mappedBy = "projects")
    @Valid
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Employee> employees;

    @JsonIgnore
    public Double getAvgSalary() {

        if (this.employees == null || this.employees.isEmpty()) {

            return 0.0;

        }

        return employees.stream().mapToDouble(Employee::getSalary).average().orElse(0.0);

    }

}
