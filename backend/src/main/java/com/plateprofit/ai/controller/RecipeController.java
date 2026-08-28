package com.plateprofit.ai.controller;

import com.plateprofit.ai.dto.RecipeItemDto; import com.plateprofit.ai.service.ResourceService;
import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/recipes")
public class RecipeController {
    private final ResourceService service; public RecipeController(ResourceService service) { this.service = service; }
    @GetMapping public ResponseEntity<List<String>> list() { return ResponseEntity.ok(service.list("recipes")); }
    @PostMapping public ResponseEntity<Void> create(@Valid @RequestBody RecipeItemDto request) { return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build(); }
}
