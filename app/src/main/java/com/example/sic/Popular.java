package com.example.sic;

public class Popular {
    private String product_image, product_title;
    private int product_price, product_favorite;
    private String product_id;

    public Popular() {
    }

    public Popular(String product_image, int product_price, String product_title, int product_favorite) {
        this.product_image = product_image;
        this.product_price = product_price;
        this.product_title = product_title;
        this.product_favorite = product_favorite;
    }
    public void changeTitle(String title){product_title = title;}

    public void changeFavorite(int favorite){product_favorite = favorite;}

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public int getProduct_favorite() {
        return product_favorite;
    }

    public void setProduct_favorite(int product_favorite) {
        this.product_favorite = product_favorite;
    }
}

