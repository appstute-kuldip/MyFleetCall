package com.example.myfleetcall.services;

public class CallDetailsRequest {

//    {
//        "startTime": "1597954208235",
//            "endTime": "1597954298235",
//            "callDuration": "90",
//            "callTo": "7350793015",
//            "callFrom": "7768948518",
//            "status": "Done"
//    }
    private long startTime;
    private String endTime;
    private long callDuration;
    private String callTo;
    private String callFrom;
    private String status;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public long getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(long callDuration) {
        this.callDuration = callDuration;
    }

    public String getCallTo() {
        return callTo;
    }

    public void setCallTo(String callTo) {
        this.callTo = callTo;
    }

    public String getCallFrom() {
        return callFrom;
    }

    public void setCallFrom(String callFrom) {
        this.callFrom = callFrom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
