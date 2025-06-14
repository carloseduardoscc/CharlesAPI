package com.carlos.charles_api.dto;

import jakarta.validation.constraints.NotBlank;

public record OpenServiceOrderRequestDTO(@NotBlank String description) {}