package com.plateprofit.ai.controller;

import com.plateprofit.ai.dto.ExpenseDto; import com.plateprofit.ai.entity.ExpenseCategory;
import com.plateprofit.ai.service.ExpenseService; import jakarta.validation.Valid;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.math.BigDecimal; import java.time.LocalDate; import java.util.List;
@RestController @RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService service; public ExpenseController(ExpenseService service) { this.service = service; }
    @GetMapping public List<ExpenseDto> list(@RequestParam Long restaurantId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate, @RequestParam(required = false) ExpenseCategory category) { return service.find(restaurantId, startDate, endDate, category); }
    @GetMapping("/{id}") public ExpenseDto get(@PathVariable Long id) { return service.get(id); }
    @PostMapping public ResponseEntity<ExpenseDto> create(@Valid @RequestBody ExpenseDto request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request)); }
    @PutMapping("/{id}") public ExpenseDto update(@PathVariable Long id, @Valid @RequestBody ExpenseDto request) { return service.update(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.delete(id); }
    @GetMapping("/total") public BigDecimal total(@RequestParam Long restaurantId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate, @RequestParam(required = false) ExpenseCategory category) { return service.total(restaurantId, startDate, endDate, category); }
}
