package com.example.demo.Service;

import com.example.demo.Modal.CheckInOutType;
import com.example.demo.Modal.History;
import com.example.demo.Modal.Vehicle;
import com.example.demo.Repository.HistoryRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class HistoryService {
    private final HistoryRepo historyRepo;

    public HistoryService(HistoryRepo historyRepo) {
        this.historyRepo = historyRepo;
    }

    public History saveHistory(Vehicle vehicle, CheckInOutType checkInOutType) {
        History history = new History();
        history.setVehicle(vehicle);
        history.setCheckInOutType(checkInOutType);
        history.setCheckInOutTime(LocalDateTime.now());
        return historyRepo.save(history);
    }

    public List<History> getAllHistory() {
        List<History> historyList = historyRepo.findAll();
        historyList.sort(Comparator.comparing(History::getCheckInOutTime).reversed());
        return historyList;
    }

    public History getHistoryByVehicle(Vehicle vehicle) {
        return historyRepo.findTopByVehicleOrderByCheckInOutTimeDesc(vehicle);
    }
}
