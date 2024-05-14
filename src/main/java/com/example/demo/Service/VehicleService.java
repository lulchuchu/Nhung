package com.example.demo.Service;

import com.example.demo.Bean.MqttBeans;
import com.example.demo.Modal.User;
import com.example.demo.Modal.Vehicle;
import com.example.demo.Repository.UserRepo;
import com.example.demo.Repository.VehicleRepo;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {
    private final VehicleRepo vehicleRepository;
    private final UserService userService;
    private final UserRepo userRepo;
    private final MqttBeans mqttBeans;

    public VehicleService(VehicleRepo vehicleRepo, UserService userService,
                          UserRepo userRepo, MqttBeans mqttBeans) {
        this.vehicleRepository = vehicleRepo;
        this.userService = userService;
        this.userRepo = userRepo;
        this.mqttBeans = mqttBeans;
    }

    public Vehicle addVehicle(String licencePlate, String userName, String cardNumber) {
        User user = new User();
        user.setName(userName);
        Vehicle vehicle = new Vehicle();
        vehicle.setLicencePlate(licencePlate);
        vehicle.setCardNumber(cardNumber);
        vehicle.setUser(user);
        userRepo.save(user);
        return vehicleRepository.save(vehicle);
    }
}
