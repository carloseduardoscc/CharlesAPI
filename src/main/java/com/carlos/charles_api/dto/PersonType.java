package com.carlos.charles_api.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PersonType {
    PF("Pessoa física"),
    PJ("Pessoa jurídica");
    private String name;
}
