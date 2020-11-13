package com.geek45.wxback.vo;

/**
 * 用户类
 */
public class User {

    private String userName;
    private String status;
    private Long lastOpTime;
    private String event;
    private String opTime;

    public String getOpTime() {
        return opTime;
    }

    public void setOpTime(String opTime) {
        this.opTime = opTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getLastOpTime() {
        return lastOpTime;
    }

    public void setLastOpTime(Long lastOpTime) {
        this.lastOpTime = lastOpTime;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
