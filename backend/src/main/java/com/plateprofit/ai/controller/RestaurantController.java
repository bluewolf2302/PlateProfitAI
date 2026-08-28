package com.plateprofit.ai.controller;

import com.plateprofit.ai.dto.RestaurantDto;
import com.plateprofit.ai.service.ResourceService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/restaurants")
public class RestaurantController {
    private final ResourceService service;
    public RestaurantController(ResourceService service) { this.service = service; }
    @GetMapping public ResponseEntity<List<String>> list() { return ResponseEntity.ok(service.list("restaurants")); }
    @PostMapping public ResponseEntity<Void> create(@Valid @RequestBody RestaurantDto request) { return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build(); }
}
