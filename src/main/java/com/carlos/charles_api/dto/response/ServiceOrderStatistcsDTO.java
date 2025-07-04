package com.carlos.charles_api.dto.response;

public record ServiceOrderStatistcsDTO(
        Integer open,
        Integer assigned,
        Integer canceled,
        Integer completed,
        Integer closed,
        Integer all
) {
}
