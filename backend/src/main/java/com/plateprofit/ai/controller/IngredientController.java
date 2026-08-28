package com.plateprofit.ai.controller;

import com.plateprofit.ai.dto.IngredientDto; import com.plateprofit.ai.service.ResourceService;
import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/ingredients")
public class IngredientController {
    private final ResourceService service; public IngredientController(ResourceService service) { this.service = service; }
    @GetMapping public ResponseEntity<List<String>> list() { return ResponseEntity.ok(service.list("ingredients")); }
    @PostMapping public ResponseEntity<Void> create(@Valid @RequestBody IngredientDto request) { return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build(); }
}
