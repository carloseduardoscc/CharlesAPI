package com.carlos.charles_api.model.enums;

//todo user role ;)
public enum FaceRole {
    ADMIN("ADMIN"),
    SUPPORTER("SUPPORTER"),
    COLLABORATOR("COLLABORATOR"),
    OWNER("OWNER");
    String name;
    FaceRole(String name){
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}
