package com.achba.studenthub.Model;

import java.util.ArrayList;
import java.util.List;

public class Roommate {

    private String id;
    private List<String> amenities ;
    private List<String> hobbies ;
    private String about;
    private String availableDate;
    private String bathRoom;
    private String bedRoom;
    private String birthday;
    private String clean;
    private String employer;
    private String gender;
    private String homeType;
    private String imageURL;
    private String nightOwl;
    private String pets;
    private String relationShip;
    private String rent;
    private String roomType;
    private String roommates;
    private String smoking ;
    private String termLength;
    private String name;

    public Roommate() {

    }

    public Roommate(String id, List<String> amenities, String about, String availableDate,
                    String bathRoom, String bedRoom, String birthday, String clean, String employer,
                    String gender, List<String> hobbies, String homeType, String imageURL, String nightOwl,
                    String pets, String relationShip, String rent, String roomType, String roommates,
                    String smoking, String termLength, String name) {
        this.id = id;
        this.amenities = amenities;
        this.about = about;
        this.availableDate = availableDate;
        this.bathRoom = bathRoom;
        this.bedRoom = bedRoom;
        this.birthday = birthday;
        this.clean = clean;
        this.employer = employer;
        this.gender = gender;
        this.hobbies = hobbies;
        this.homeType = homeType;
        this.imageURL = imageURL;
        this.nightOwl = nightOwl;
        this.pets = pets;
        this.relationShip = relationShip;
        this.rent = rent;
        this.roomType = roomType;
        this.roommates = roommates;
        this.smoking = smoking;
        this.termLength = termLength;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public String getAbout() {
        return about;
    }

    public String getAvailableDate() {
        return availableDate;
    }

    public String getBathRoom() {
        return bathRoom;
    }

    public String getBedRoom() {
        return bedRoom;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getClean() {
        return clean;
    }

    public String getEmployer() {
        return employer;
    }

    public String getGender() {
        return gender;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public String getHomeType() {
        return homeType;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getNightOwl() {
        return nightOwl;
    }

    public String getPets() {
        return pets;
    }

    public String getRelationShip() {
        return relationShip;
    }

    public String getRent() {
        return rent;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoommates() {
        return roommates;
    }

    public String getSmoking() {
        return smoking;
    }

    public String getTermLength() {
        return termLength;
    }

    public String getName() {
        return name;
    }
}