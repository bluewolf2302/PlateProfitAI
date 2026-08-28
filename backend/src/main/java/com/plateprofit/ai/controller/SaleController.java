package com.plateprofit.ai.controller;

import com.plateprofit.ai.dto.SaleDto; import com.plateprofit.ai.dto.SaleCreateDto;
import com.plateprofit.ai.service.ResourceCrudService; import com.plateprofit.ai.service.SalesProcessingService;
import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/sales")
public class SaleController {
    private final ResourceCrudService service;
    private final SalesProcessingService salesProcessingService;
    public SaleController(ResourceCrudService service, SalesProcessingService salesProcessingService) { this.service = service; this.salesProcessingService = salesProcessingService; }
    @GetMapping public List<SaleDto> list() { return service.sales(); }
    @GetMapping("/{id}") public SaleDto get(@PathVariable Long id) { return service.sale(id); }
    @PostMapping public ResponseEntity<SaleDto> create(@Valid @RequestBody SaleCreateDto request) { return ResponseEntity.status(HttpStatus.CREATED).body(salesProcessingService.processSale(request)); }
    @PutMapping("/{id}") public SaleDto update(@PathVariable Long id, @Valid @RequestBody SaleDto request) { return service.update(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.deleteSale(id); }
}
