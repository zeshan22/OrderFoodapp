package com.example.orderfoodapp.Model;

import android.widget.ImageView;

public class Category {

    private String Name;
    private String Image;

    public  Category(){


    }

    public void Category(String name,String image){

        Name=name;
        Image=image;

    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getImage() {
        return Image;
    }
}
