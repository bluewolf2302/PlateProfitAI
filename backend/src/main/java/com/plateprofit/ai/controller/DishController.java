package com.plateprofit.ai.controller;

import com.plateprofit.ai.dto.DishDto; import com.plateprofit.ai.dto.DishCostDto;
import com.plateprofit.ai.service.DishCostingService; import com.plateprofit.ai.service.ResourceCrudService;
import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/dishes")
public class DishController {
    private final ResourceCrudService service;
    private final DishCostingService costingService;
    public DishController(ResourceCrudService service, DishCostingService costingService) { this.service = service; this.costingService = costingService; }
    @GetMapping public List<DishDto> list() { return service.dishes(); }
    @GetMapping("/{id}") public DishDto get(@PathVariable Long id) { return service.dish(id); }
    @GetMapping("/{id}/cost") public DishCostDto cost(@PathVariable Long id) { return costingService.calculateCost(id); }
    @PostMapping public ResponseEntity<DishDto> create(@Valid @RequestBody DishDto request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request)); }
    @PutMapping("/{id}") public DishDto update(@PathVariable Long id, @Valid @RequestBody DishDto request) { return service.update(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.deleteDish(id); }
}
