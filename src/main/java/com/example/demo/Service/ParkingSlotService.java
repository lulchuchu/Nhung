package com.example.demo.Service;

import com.example.demo.Modal.ParkingSlot;
import com.example.demo.Repository.ParkingSlotRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingSlotService {
    private final ParkingSlotRepo parkingSlotRepo;

    public ParkingSlotService(ParkingSlotRepo parkingSlotRepo) {
        this.parkingSlotRepo = parkingSlotRepo;
    }

    public List<ParkingSlot> getAllParkingSlots() {
        return parkingSlotRepo.findAll();
    }
}
