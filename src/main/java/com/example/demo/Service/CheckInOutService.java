package com.example.demo.Service;

import com.example.demo.Gateway.MqttGateWay;
import com.example.demo.Modal.CheckInOutType;
import com.example.demo.Modal.History;
import com.example.demo.Modal.Vehicle;
import com.example.demo.Repository.VehicleRepo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CheckInOutService implements CheckInOutServiceInterface {
    private final VehicleRepo vehicleRepo;
    private final HistoryService historyService;
    private final MqttGateWay mqttGateWay;
    private final SimpMessagingTemplate messagingTemplate;


    public CheckInOutService(VehicleRepo vehicleRepo, HistoryService historyService, MqttGateWay mqttGateWay, SimpMessagingTemplate messagingTemplate) {
        this.vehicleRepo = vehicleRepo;
        this.historyService = historyService;
        this.mqttGateWay = mqttGateWay;
        this.messagingTemplate = messagingTemplate;
    }

    private void sendToTopic (History history) {
        String sensorTopic = "/topic/history";
        messagingTemplate.convertAndSend(sensorTopic, history);
    }

    public String checkInOut(String cardNumber, String licensePlate) {
        System.out.println("CHECKING IN OUT"+ cardNumber + licensePlate);
        Optional<Vehicle> vehicle = vehicleRepo.findByCardNumber(cardNumber);
        if (!vehicle.isPresent()) {
            String topic = "fault_detected";
            try {
                mqttGateWay.sendToMqtt("Invalid cardNumber", topic);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Vehicle not found");
            return "Vehicle not found";
        }

        boolean isValid = Objects.equals(vehicle.get().getLicencePlate(), licensePlate);
        if (!isValid) {
            String topic = "fault_detected";
            try {
                mqttGateWay.sendToMqtt("Invalid licensePlate", topic);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Invalid license plate");

            return "Invalid license plate";
        }

        String topic = "servo";
        try {
            mqttGateWay.sendToMqtt("Servo1 180", topic);
        } catch (Exception e) {
            e.printStackTrace();
        }

        History latestHistoryOfVehicle = historyService.getHistoryByVehicle(vehicle.get());
        if (latestHistoryOfVehicle == null || latestHistoryOfVehicle.getCheckInOutType() == CheckInOutType.CHECKOUT) {
            History history= historyService.saveHistory(vehicle.get(), CheckInOutType.CHECKIN);
            sendToTopic(history);

            return "Checkin successful";
        }
        if (latestHistoryOfVehicle.getCheckInOutType().equals(CheckInOutType.CHECKIN)) {
            History history=historyService.saveHistory(vehicle.get(), CheckInOutType.CHECKOUT);
            sendToTopic(history);
            return "Checkout successful";
        }
        return "Something went wrong";
    }
}
