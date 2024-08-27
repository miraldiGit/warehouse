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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SQLRestriction("deleted=false")
@Table(name="t_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "last_name", nullable = false)
    @NotNull
    private String lastName;

    @Column(name = "first_name", nullable = false)
    @NotNull
    private String firstName;

    @Column(name = "email", unique = true, nullable = false)
    @NotNull
    private String email;

    @Column(name = "city")
    private String city;

    @Column(name = "postal_code")
    @Min(1000)
    @Max(999999)
    private Integer postalCode;

    @Column(name = "country", nullable = false)
    @NotNull
    private String country;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email);
    }
}
