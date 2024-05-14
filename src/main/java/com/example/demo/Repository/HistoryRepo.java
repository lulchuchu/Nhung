package com.example.demo.Repository;

import com.example.demo.Modal.History;
import com.example.demo.Modal.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepo extends JpaRepository<History, Long> {
    History findTopByVehicleOrderByCheckInOutTimeDesc(Vehicle vehicle);
}
