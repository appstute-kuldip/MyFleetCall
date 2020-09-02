package com.example.myfleetcall.services;

public class ResponsePlaceCall {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponsePlaceCall{" +
                "message='" + message + '\'' +
                '}';
    }
}
