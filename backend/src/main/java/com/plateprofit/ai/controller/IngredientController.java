package com.plateprofit.ai.controller;

import com.plateprofit.ai.dto.IngredientDto; import com.plateprofit.ai.service.ResourceCrudService;
import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/ingredients")
public class IngredientController {
    private final ResourceCrudService service; public IngredientController(ResourceCrudService service) { this.service = service; }
    @GetMapping public List<IngredientDto> list() { return service.ingredients(); }
    @GetMapping("/{id}") public IngredientDto get(@PathVariable Long id) { return service.ingredient(id); }
    @PostMapping public ResponseEntity<IngredientDto> create(@Valid @RequestBody IngredientDto request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request)); }
    @PutMapping("/{id}") public IngredientDto update(@PathVariable Long id, @Valid @RequestBody IngredientDto request) { return service.update(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.deleteIngredient(id); }
}
