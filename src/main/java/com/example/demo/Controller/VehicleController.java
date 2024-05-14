package com.example.demo.Controller;

import com.example.demo.Modal.Vehicle;
import com.example.demo.Modal.VehicleDTO;
import com.example.demo.Service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicle")
@CrossOrigin
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }
    @PostMapping("")
    public ResponseEntity<?> addVehicle(@RequestBody VehicleDTO vehicleDTO) {
        vehicleService.addVehicle(vehicleDTO.getLicencePlate(), vehicleDTO.getUserName(), vehicleDTO.getCardNumber());
        return ResponseEntity.ok("Vehicle added successfully");
    }

}
