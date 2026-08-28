package com.plateprofit.ai.controller;

import com.plateprofit.ai.dto.ExpenseDto; import com.plateprofit.ai.service.ResourceService;
import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/expenses")
public class ExpenseController {
    private final ResourceService service; public ExpenseController(ResourceService service) { this.service = service; }
    @GetMapping public ResponseEntity<List<String>> list() { return ResponseEntity.ok(service.list("expenses")); }
    @PostMapping public ResponseEntity<Void> create(@Valid @RequestBody ExpenseDto request) { return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build(); }
}
