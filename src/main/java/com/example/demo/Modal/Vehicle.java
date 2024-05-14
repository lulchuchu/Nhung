package com.example.demo.Modal;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String licencePlate;
    private String cardNumber;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
