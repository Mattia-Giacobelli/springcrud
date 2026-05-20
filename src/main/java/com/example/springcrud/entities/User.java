package com.example.springcrud.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email obbligatoria")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email non valida")
    private String email;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Username obbligatorio")

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=(?:.*\\d){2})(?=(?:.*[?@_-]){2})[A-Za-z\\d?@_-]{8,10}$", message = "Deve essere tra 8 e 10 caratteri,uno maiusc,uno minusc, 2 numeri,2 caratteri speciali ?@_-")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "Password obbligatoria")
    private String password;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    @Valid
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Employee employee;

    // tolto constrained per testare
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "permission_id", nullable = false)
    @Valid
    @NotNull(message = "campo obbligatorio")
    private Permission permission;

}
