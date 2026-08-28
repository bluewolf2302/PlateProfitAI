package com.plateprofit.ai.controller;

import com.plateprofit.ai.dto.DishDto; import com.plateprofit.ai.service.ResourceService;
import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/dishes")
public class DishController {
    private final ResourceService service; public DishController(ResourceService service) { this.service = service; }
    @GetMapping public ResponseEntity<List<String>> list() { return ResponseEntity.ok(service.list("dishes")); }
    @PostMapping public ResponseEntity<Void> create(@Valid @RequestBody DishDto request) { return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build(); }
}
