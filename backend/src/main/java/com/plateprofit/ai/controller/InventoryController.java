package com.plateprofit.ai.controller;

import com.plateprofit.ai.dto.InventoryDto; import com.plateprofit.ai.service.ResourceService;
import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/inventory")
public class InventoryController {
    private final ResourceService service; public InventoryController(ResourceService service) { this.service = service; }
    @GetMapping public ResponseEntity<List<String>> list() { return ResponseEntity.ok(service.list("inventory")); }
    @PostMapping public ResponseEntity<Void> create(@Valid @RequestBody InventoryDto request) { return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build(); }
}
