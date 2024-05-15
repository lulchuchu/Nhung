package com.example.demo.Service;

import com.example.demo.Modal.ParkingSlot;
import com.example.demo.Repository.ParkingSlotRepo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingSlotService {
    private final ParkingSlotRepo parkingSlotRepo;
    private final SimpMessagingTemplate messagingTemplate;

    public ParkingSlotService(ParkingSlotRepo parkingSlotRepo, SimpMessagingTemplate messagingTemplate) {
        this.parkingSlotRepo = parkingSlotRepo;
        this.messagingTemplate = messagingTemplate;
    }

    public List<ParkingSlot> getAllParkingSlots() {
        return parkingSlotRepo.findAll();
    }
    private void sendToTopic(List<ParkingSlot> parkingSlots) {
        String sensorTopic = "/topic/parkingSlot";
        messagingTemplate.convertAndSend(sensorTopic, parkingSlots);
    }
    public void updateParkingSlot (List<String> parkingSlots) {

        for (int i = 0; i < parkingSlots.size(); i++) {
            ParkingSlot parkingSlot = parkingSlotRepo.findBySlotName("park_" + (i + 1));
            parkingSlot.setOccupied(Boolean.parseBoolean(parkingSlots.get(i)));
            parkingSlotRepo.save(parkingSlot);
        }
        List<ParkingSlot> parkingSlots1 = parkingSlotRepo.findAll();
        sendToTopic(parkingSlots1);
    }
}
