package com.example.demo.Modal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ParkingSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String slotName;
    private boolean isOccupied;
}
