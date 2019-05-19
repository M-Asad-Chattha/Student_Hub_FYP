package com.achba.studenthub;

public class Notification {
    private String userID;
    private String text;

    public Notification(String userID, String text){
        this.userID=userID;
        this.text=text;
    }

    public Notification() {

    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
