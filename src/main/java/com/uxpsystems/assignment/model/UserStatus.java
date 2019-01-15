package com.uxpsystems.assignment.model;

public enum UserStatus {
    ACTIVATED("Activated"),DEACTIVATED("Deactivated");
    String status;
    private UserStatus(String status){
        this.status = status;
    }
    public String getStatus() {
        return this.status;
    }
}
