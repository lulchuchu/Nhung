package com.example.demo.Modal;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoryDTO {
    private Long id;
    private CheckInOutType checkInOutType;
    private LocalDateTime checkInOutTime;
    private Vehicle vehicle;
}
