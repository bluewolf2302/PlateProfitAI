package com.plateprofit.ai.controller;

import com.plateprofit.ai.dto.SaleDto; import com.plateprofit.ai.service.ResourceCrudService;
import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/sales")
public class SaleController {
    private final ResourceCrudService service; public SaleController(ResourceCrudService service) { this.service = service; }
    @GetMapping public List<SaleDto> list() { return service.sales(); }
    @GetMapping("/{id}") public SaleDto get(@PathVariable Long id) { return service.sale(id); }
    @PostMapping public ResponseEntity<SaleDto> create(@Valid @RequestBody SaleDto request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request)); }
    @PutMapping("/{id}") public SaleDto update(@PathVariable Long id, @Valid @RequestBody SaleDto request) { return service.update(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.deleteSale(id); }
}
