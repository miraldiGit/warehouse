package com.miraldi.warehouse.entities;

import com.miraldi.warehouse.utils.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @NotNull
    private Role role;

    @Column(name = "username", unique = true, nullable = false)
    @NotNull
    private String username;

    @Column(name = "password", nullable = false)
    @NotNull
    private String password;

    @Column(name = "lastName", nullable = false)
    @NotNull
    private String lastName;

    @Column(name = "firstName", nullable = false)
    @NotNull
    private String firstName;

    @Column(name = "email", unique = true, nullable = false)
    @NotNull
    private String email;

    @Column(name = "city")
    private String city;

    @Column(name = "postalCode")
    @Min(1000)
    @Max(999999)
    private int postalCode;

    @Column(name = "country", nullable = false)
    @NotNull
    private String country;

    @Column(name = "securityQuestion", nullable = false)
    @NotNull
    private String securityQuestion;

    @Column(name = "answerOfQuestion", nullable = false)
    @NotNull
    private String answerOfQuestion;

    @Column(name = "disabled", nullable = false)
    @NotNull
    private boolean disabled;
}
