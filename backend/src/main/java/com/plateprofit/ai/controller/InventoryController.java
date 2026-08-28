package com.plateprofit.ai.controller;

import com.plateprofit.ai.dto.InventoryDto; import com.plateprofit.ai.service.ResourceCrudService;
import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/inventory")
public class InventoryController {
    private final ResourceCrudService service; public InventoryController(ResourceCrudService service) { this.service = service; }
    @GetMapping public List<InventoryDto> list() { return service.inventory(); }
    @GetMapping("/{id}") public InventoryDto get(@PathVariable Long id) { return service.inventoryItem(id); }
    @PostMapping public ResponseEntity<InventoryDto> create(@Valid @RequestBody InventoryDto request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request)); }
    @PutMapping("/{id}") public InventoryDto update(@PathVariable Long id, @Valid @RequestBody InventoryDto request) { return service.update(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.deleteInventory(id); }
}
