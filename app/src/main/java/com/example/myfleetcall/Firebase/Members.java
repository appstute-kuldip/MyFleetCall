package com.example.myfleetcall.Firebase;

public class Members {

    String sim_Id;
    String device_Id;

    public Members() {
    }

    public Members(String sim_Id, String device_Id) {
        this.sim_Id = sim_Id;
        this.device_Id = device_Id;
    }

    public String getSim_Id() {
        return sim_Id;
    }

    public void setSim_Id(String sim_Id) {
        this.sim_Id = sim_Id;
    }

    public String getDevice_Id() {
        return device_Id;
    }

    public void setDevice_Id(String device_Id) {
        this.device_Id = device_Id;
    }
}
