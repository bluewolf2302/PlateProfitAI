package com.plateprofit.ai.controller;

import com.plateprofit.ai.dto.SaleDto; import com.plateprofit.ai.service.ResourceService;
import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/sales")
public class SaleController {
    private final ResourceService service; public SaleController(ResourceService service) { this.service = service; }
    @GetMapping public ResponseEntity<List<String>> list() { return ResponseEntity.ok(service.list("sales")); }
    @PostMapping public ResponseEntity<Void> create(@Valid @RequestBody SaleDto request) { return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build(); }
}
