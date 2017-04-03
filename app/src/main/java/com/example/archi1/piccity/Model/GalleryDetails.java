package com.example.archi1.piccity.Model;

/**
 * Created by archi1 on 11/25/2016.
 */

public class GalleryDetails {

    private String id;
    private String name;
    private String image;
    private String canvas_image;
    private String size;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCanvas_image() {
        return canvas_image;
    }

    public void setCanvas_image(String canvas_image) {
        this.canvas_image = canvas_image;
    }
}
