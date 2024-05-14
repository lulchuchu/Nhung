package com.example.demo.Repository;

import com.example.demo.Modal.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepo extends JpaRepository<Vehicle, String> {
    public Optional<Vehicle> findByLicencePlate(String licencePlate);
    public Optional<Vehicle> findByCardNumber(String cardNumber);

}
