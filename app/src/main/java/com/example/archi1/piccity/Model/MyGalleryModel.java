package com.example.archi1.piccity.Model;

/**
 * Created by archi on 30-Mar-17.
 */

public class MyGalleryModel {
    private String id;
    private String name;
    private String image;
    private String price,canvasImage;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCanvasImage() {
        return canvasImage;
    }

    public void setCanvasImage(String canvasImage) {
        this.canvasImage = canvasImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getimage() {
        return image;
    }

    public void setimage(String img) {
        this.image = img;
    }
}
