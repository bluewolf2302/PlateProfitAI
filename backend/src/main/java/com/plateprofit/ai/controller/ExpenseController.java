package com.plateprofit.ai.controller;

import com.plateprofit.ai.dto.ExpenseDto; import com.plateprofit.ai.service.ResourceCrudService;
import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/expenses")
public class ExpenseController {
    private final ResourceCrudService service; public ExpenseController(ResourceCrudService service) { this.service = service; }
    @GetMapping public List<ExpenseDto> list() { return service.expenses(); }
    @GetMapping("/{id}") public ExpenseDto get(@PathVariable Long id) { return service.expense(id); }
    @PostMapping public ResponseEntity<ExpenseDto> create(@Valid @RequestBody ExpenseDto request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request)); }
    @PutMapping("/{id}") public ExpenseDto update(@PathVariable Long id, @Valid @RequestBody ExpenseDto request) { return service.update(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.deleteExpense(id); }
}
