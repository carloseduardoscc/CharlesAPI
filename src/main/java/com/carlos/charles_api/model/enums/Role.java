package com.carlos.charles_api.model.enums;

public enum Role {
    ADMIN("ADMIN"),
    SUPPORTER("SUPPORTER"),
    COLLABORATOR("COLLABORATOR"),
    OWNER("OWNER");
    String name;
    Role(String name){
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}
