package com.miraldi.warehouse.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLRestriction("deleted=false")
@Table(name="t_truck")
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "chassis_number", unique = true, nullable = false)
    @NotNull
    private String chassisNumber;

    @Column(name="license_plate", unique = true, nullable = false)
    @NotNull
    private String licensePlate;

    @Column(name = "items_quantity_in_truck", nullable = false)
    @NotNull
    private Integer itemsQuantityInTruck;

    @Column(name="deleted")
    private boolean deleted;

    @ManyToMany(mappedBy = "trucks")
    private Set<Order> orders = new HashSet<>();

    @OneToMany(mappedBy = "truck", cascade = CascadeType.ALL)
    private Set<TruckBookingDate> bookingDates = new HashSet<>();
}
