package com.achba.studenthub.Model;

import java.util.List;

public class Conveyance {

    private String id;
    private String name;
    private String profileImageUrl;
    private String address;
    private String phoneNumber;
    private String cost;
    private String seats;
    private String location;


    public Conveyance() {

    }

    public Conveyance(String id, String name, String profileImageUrl, String address, String phoneNumber,
                      String cost, String seats, String location) {
        this.id = id;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.cost = cost;
        this.seats = seats;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCost() {
        return cost;
    }

    public String getSeats() {
        return seats;
    }

    public String getLocation() {
        return location;
    }
}
