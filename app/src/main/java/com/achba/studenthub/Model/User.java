package com.achba.studenthub.Model;

public class User {

    private String id;
    private String bio;
    private String userName;
    private String name;
    private String profileImageUrl;
    private String program;
    private String semester;
    private String section;
    private String campus;
    private String email;

    public User(){

    }

    public User(String bio, String userName, String name, String profileImageUrl,
                String program, String semester, String section, String campus, String email, String id) {
        this.bio = bio;
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.program = program;
        this.semester = semester;
        this.section = section;
        this.campus = campus;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getBio() {
        return bio;
    }

    public String getUserName() {
        return userName;
    }

    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getProgram() {
        return program;
    }

    public String getSemester() {
        return semester;
    }

    public String getSection() {
        return section;
    }

    public String getCampus() {
        return campus;
    }

    public String getEmail() {
        return email;
    }
}