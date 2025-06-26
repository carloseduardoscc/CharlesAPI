package com.carlos.charles_api.model.enums;

//todo user role ;)
public enum UserRole {
    ADMIN("ADMIN"),
    SUPPORTER("SUPPORTER"),
    COLLABORATOR("COLLABORATOR"),
    OWNER("OWNER");
    String name;
    UserRole(String name){
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}
