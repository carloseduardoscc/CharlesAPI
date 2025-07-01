package com.carlos.charles_api.model.enums;

public enum SoStateType {
    OPEN("Aberta"),
    ASSIGNED("Atribuída"),
    COMPLETED("Completada"),
    CANCELED("Cancelada");

    String name;

    SoStateType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
