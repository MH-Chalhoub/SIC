package com.example.sic;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

public class Item implements Parcelable {
    private ArrayList<String> images;
    String title;
    String category;
    String description;
    String location;
    String name;
    String email;
    String phone;
    Float price;
    Date posted_time;
    int views;
    String userId;

    public Item() {
    }

    public Item(Parcel in) {

        ArrayList<String> imagesPictureUrls = new ArrayList<>();
        in.readStringList(imagesPictureUrls);
        images = imagesPictureUrls;
        this.title = in.readString();
        this.category = in.readString();
        this.description = in.readString();
        this.location = in.readString();
        this.name = in.readString();
        this.email = in.readString();
        this.phone = in.readString();
        this.price = in.readFloat();
        this.posted_time = (Date) in.readSerializable();
        this.views = in.readInt();
        this.userId = in.readString();
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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Date getPosted_time() {
        return posted_time;
    }

    public void setPosted_time(Date posted_time) {
        this.posted_time = posted_time;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
//I am using Parcelable to send the object (Item) using Intent.putExtra

    public Item(ArrayList<String> images, String title, String category, String description, String location, String name, String email, String phone, Float price, Date posted_time, int views, String userId) {
        this.images = images;
        this.title = title;
        this.category = category;
        this.description = description;
        this.location = location;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.price = price;
        this.posted_time = posted_time;
        this.views = views;
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(images);
        dest.writeString(title);
        dest.writeString(category);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeFloat(price);
        dest.writeSerializable(posted_time);
        dest.writeInt(views);
        dest.writeString(userId);
    }
    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

}
