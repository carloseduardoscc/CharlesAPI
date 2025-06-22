package com.carlos.charles_api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record OpenServiceOrderDTO(@NotBlank String description) {}