package com.carlos.charles_api.dto.response;

public record ServiceOrderStatistcsDTO(
        long open,
        long assigned,
        long canceled,
        long completed,
        long closed,
        long all
) {
}
