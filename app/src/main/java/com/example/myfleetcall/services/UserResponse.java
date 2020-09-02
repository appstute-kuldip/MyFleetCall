package com.example.myfleetcall.services;

public class UserResponse {


    private String message;
    private String id;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "message='" + message + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
