package com.example.myfleetcall.services;

public class UserRequest {


    private String device_Id;
    private String device_Id_2;
    private String sim_Id;
    private String mobileNumber;
    private String fcmToken;


    public String getDevice_Id() {
        return device_Id;
    }

    public void setDevice_Id(String device_Id) {
        this.device_Id = device_Id;
    }

    public String getDevice_Id_2() {
        return device_Id_2;
    }

    public void setDevice_Id_2(String device_Id_2) {
        this.device_Id_2 = device_Id_2;
    }

    public String getSim_Id() {
        return sim_Id;
    }

    public void setSim_Id(String sim_Id) {
        this.sim_Id = sim_Id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
