package com.miraldi.warehouse.entities;

import com.miraldi.warehouse.utils.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("deleted=false")
@Table(name="order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_number")
    private Long orderNumber;

    @Column(name = "submitted_date", nullable = false)
    @NotNull
    private LocalDate submittedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull
    private Status status;

    @Column(name = "deadline_date", nullable = false)
    @NotNull
    private LocalDate deadlineDate;

    @Column(name="deleted")
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private Set<OrderItem> orderItems = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "order_truck",
            joinColumns = @JoinColumn(name = "order_number"),
            inverseJoinColumns = @JoinColumn(name = "truck_id")
    )
    private Set<Truck> trucks = new HashSet<>();
}
