package com.plateprofit.ai.controller;

import com.plateprofit.ai.dto.RestaurantDto;
import com.plateprofit.ai.service.ResourceCrudService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/restaurants")
public class RestaurantController {
    private final ResourceCrudService service;
    public RestaurantController(ResourceCrudService service) { this.service = service; }
    @GetMapping public List<RestaurantDto> list() { return service.restaurants(); }
    @GetMapping("/{id}") public RestaurantDto get(@PathVariable Long id) { return service.restaurant(id); }
    @PostMapping public ResponseEntity<RestaurantDto> create(@Valid @RequestBody RestaurantDto request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request)); }
    @PutMapping("/{id}") public RestaurantDto update(@PathVariable Long id, @Valid @RequestBody RestaurantDto request) { return service.update(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.deleteRestaurant(id); }
}
