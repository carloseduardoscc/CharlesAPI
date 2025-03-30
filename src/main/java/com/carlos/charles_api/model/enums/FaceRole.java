package com.carlos.charles_api.model.enums;

public enum FaceRole {
    ADMIN("ADMIN"),
    SUPPORTER("SUPPORTER"),
    COLLABORATOR("COLLABORATOR");
    String name;
    FaceRole(String name){
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}
