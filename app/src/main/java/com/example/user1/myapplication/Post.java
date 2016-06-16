package com.example.user1.myapplication;

public class Post {
    private String name;
    private String timeStamp;
    private String status;
    private int id;
    private int img;
    private int profile;

    public String getName() {
        return name;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getImg() {
        return img;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public int getProfile() {
        return profile;
    }
}
