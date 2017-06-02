package com.example.archi1.piccity.Model;

/**
 * Created by archi1 on 11/26/2016.
 */

public class ArtGallery {

    private String id;
    private String image;
    private String price;
    private String description;
    private String imageFeedBackId;
    private String imageFeedBackText;
    private String location;
    private String name;
    private String username;
    private String userProfilePic;

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    private String userId;
    private String category;
    private String currency;
    private String paypalEmail;

    public String getImageFeedBackId() {
        return imageFeedBackId;
    }

    public void setImageFeedBackId(String imageFeedBackId) {
        this.imageFeedBackId = imageFeedBackId;
    }

    public String getImageFeedBackText() {
        return imageFeedBackText;
    }

    public void setImageFeedBackText(String imageFeedBackText) {
        this.imageFeedBackText = imageFeedBackText;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaypalEmail() {
        return paypalEmail;
    }

    public void setPaypalEmail(String paypalEmail) {
        this.paypalEmail = paypalEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
}
