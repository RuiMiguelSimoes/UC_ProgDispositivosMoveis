package com.example.happens;

import android.graphics.Bitmap;

public class ModelClass {
    String imageName;
    Bitmap image;

    public ModelClass(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
