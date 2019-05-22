package com.example.sic;

public class Category {
    private String catname, caticon, catbg, cattitlebg;

    public Category() {
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getCaticon() {
        return caticon;
    }

    public void setCaticon(String caticon) {
        this.caticon = caticon;
    }

    public String getCatbg() {
        return catbg;
    }

    public void setCatbg(String catbg) {
        this.catbg = catbg;
    }

    public String getCattitlebg() {
        return cattitlebg;
    }

    public void setCattitlebg(String cattitlebg) {
        this.cattitlebg = cattitlebg;
    }

    public Category(String catname, String caticon, String catbg, String cattitlebg) {
        this.catname = catname;
        this.caticon = caticon;
        this.catbg = catbg;
        this.cattitlebg = cattitlebg;
    }
}
