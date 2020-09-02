package com.example.myfleetcall.services;

public class CheckValidityRequest {


    private String mobileNumber;
    private String simId;
    private String deviceId;
    private String deviceId_2;
    private String id;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getSimId() {
        return simId;
    }

    public void setSimId(String simId) {
        this.simId = simId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId_2() {
        return deviceId_2;
    }

    public void setDeviceId_2(String deviceId_2) {
        this.deviceId_2 = deviceId_2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
