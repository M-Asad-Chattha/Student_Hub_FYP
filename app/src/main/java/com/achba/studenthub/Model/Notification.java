package com.achba.studenthub.Model;

public class Notification {

    private String id;
    private String timeStamp;
    private String description;
    private String name;
    private String status;
    private String type;
    private String profileImageUrl;

    public Notification(){

    }


    public Notification(String description, String name, String id, String status, String timeStamp,
                        String profileImageUrl, String type) {
        this.description = description;
        this.id = id;
        this.name = name;
        this.status = status;
        this.type = type;
        this.timeStamp = timeStamp;
        this.profileImageUrl = profileImageUrl;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getType() {
        return type;
    }
}