package com.example.demo.Repository;

import com.example.demo.Modal.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSlotRepo extends JpaRepository<ParkingSlot, Long>{
    public List<ParkingSlot> findByIsOccupied(boolean isOccupied);
    public ParkingSlot findBySlotName(String slotName);
}
