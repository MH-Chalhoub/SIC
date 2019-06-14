package com.example.sic;

public class User {
    String firstname, lastname, email, contactno;
    int itemsCount, favoriteItemsCount;

    public User() {
    }

    public User(String firstname, String lastname, String email, String contactno, int itemsCount, int favoriteItemsCount) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.contactno = contactno;
        this.itemsCount = itemsCount;
        this.favoriteItemsCount = favoriteItemsCount;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public int getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(int itemsCount) {
        this.itemsCount = itemsCount;
    }

    public int getFavoriteItemsCount() {
        return favoriteItemsCount;
    }

    public void setFavoriteItemsCount(int favoriteItemsCount) {
        this.favoriteItemsCount = favoriteItemsCount;
    }
}
