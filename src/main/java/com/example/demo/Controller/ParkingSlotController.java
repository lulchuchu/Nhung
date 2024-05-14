package com.example.demo.Controller;

import com.example.demo.Modal.ParkingSlot;
import com.example.demo.Service.ParkingSlotService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/parkingSlot")
@CrossOrigin
public class ParkingSlotController {
    private final ParkingSlotService parkingSlotService;

    public ParkingSlotController(ParkingSlotService parkingSlotService) {
        this.parkingSlotService = parkingSlotService;
    }

    @GetMapping("/parkingSlot")
    public List<ParkingSlot> getAllParkingSlots() {
        return parkingSlotService.getAllParkingSlots();
    }
}
