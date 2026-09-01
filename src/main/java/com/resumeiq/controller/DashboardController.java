package com.resumeiq.controller;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.resumeiq.dto.DashboardResponse;

import com.resumeiq.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:5173")
public class DashboardController {

    private final DashboardService
            dashboardService;


    public DashboardController(
            DashboardService dashboardService) {

        this.dashboardService =
                dashboardService;
    }


    @GetMapping
    public ResponseEntity<DashboardResponse>
            getDashboard() {

        return ResponseEntity.ok(
                dashboardService
                        .getDashboard()
        );
    }
}