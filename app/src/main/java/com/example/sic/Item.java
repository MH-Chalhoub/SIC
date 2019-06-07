package com.example.sic;

import java.util.ArrayList;

public class Item {
    private ArrayList<String> images;
    String title;
    String category;
    String description;
    String location;
    String name;
    String email;
    String phone;

    public Item() {
    }

    public Item(ArrayList<String> images, String title, String category, String description, String location, String name, String email, String phone) {
        this.images = images;
        this.title = title;
        this.category = category;
        this.description = description;
        this.location = location;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
