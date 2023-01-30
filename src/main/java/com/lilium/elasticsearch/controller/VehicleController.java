package com.lilium.elasticsearch.controller;

import com.lilium.elasticsearch.document.Vehicle;
import com.lilium.elasticsearch.service.VehicleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    @PostMapping
    public void index(@RequestBody Vehicle vehicle) {
        service.index(vehicle);
    }

    @GetMapping("/{id}")
    public Vehicle getById(@PathVariable String id) {
        return service.getById(id);
    }

}
