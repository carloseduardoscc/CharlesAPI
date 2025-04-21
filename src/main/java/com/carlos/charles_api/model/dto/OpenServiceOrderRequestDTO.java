package com.carlos.charles_api.model.dto;

import jakarta.validation.constraints.NotBlank;

public record OpenServiceOrderRequestDTO(@NotBlank String description) {}