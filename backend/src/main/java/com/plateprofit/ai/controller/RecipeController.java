package com.plateprofit.ai.controller;

import com.plateprofit.ai.dto.RecipeItemDto; import com.plateprofit.ai.service.ResourceCrudService;
import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/recipes")
public class RecipeController {
    private final ResourceCrudService service; public RecipeController(ResourceCrudService service) { this.service = service; }
    @GetMapping public List<RecipeItemDto> list() { return service.recipes(); }
    @GetMapping("/{id}") public RecipeItemDto get(@PathVariable Long id) { return service.recipe(id); }
    @PostMapping public ResponseEntity<RecipeItemDto> create(@Valid @RequestBody RecipeItemDto request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request)); }
    @PutMapping("/{id}") public RecipeItemDto update(@PathVariable Long id, @Valid @RequestBody RecipeItemDto request) { return service.update(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.deleteRecipe(id); }
}
