package com.plateprofit.ai.dto;

import jakarta.validation.constraints.NotBlank;
public record RestaurantDto(Long id, @NotBlank String name, String address) { }
