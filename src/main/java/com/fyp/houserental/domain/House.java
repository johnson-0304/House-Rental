package com.fyp.houserental.domain;

import org.springframework.core.SpringVersion;

import java.util.List;

public class House {
    private int ID;
    private String buildingName;
    private String address;
    private Integer rental;
    private String state;
    private String description;
    private String type;
    private Integer squareFt;
    private String furnish;
    private Integer carPark;
    private Integer bedroom;
    private Integer bathroom;
    private String status;
    private String owner_name;
    private String agreement;
    private int houseOwnerId;
    private String imageUrl;
    private String MainImageUrl;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMainImageUrl() {
        return MainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        MainImageUrl = mainImageUrl;
    }

    public int getHouseOwnerId() {
        return houseOwnerId;
    }

    public void setHouseOwnerId(int houseOwnerId) {
        this.houseOwnerId = houseOwnerId;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //    private List<String> Images;


    public String getImageUrl() {
        return imageUrl;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "House{" +
                "ID=" + ID +
                ", buildingName='" + buildingName + '\'' +
                ", address='" + address + '\'' +
                ", rental=" + rental +
                ", state='" + state + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", squareFt=" + squareFt +
                ", furnish='" + furnish + '\'' +
                ", carPark=" + carPark +
                ", bedroom=" + bedroom +
                ", bathroom=" + bathroom +
                ", status='" + status + '\'' +
                ", owner_name='" + owner_name + '\'' +
                ", agreement='" + agreement + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getRental() {
        return rental;
    }

    public void setRental(Integer rental) {
        this.rental = rental;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSquareFt() {
        return squareFt;
    }

    public void setSquareFt(Integer squareFt) {
        this.squareFt = squareFt;
    }

    public String getFurnish() {
        return furnish;
    }

    public void setFurnish(String furnish) {
        this.furnish = furnish;
    }

    public Integer getCarPark() {
        return carPark;
    }

    public void setCarPark(Integer carPark) {
        this.carPark = carPark;
    }

    public Integer getBedroom() {
        return bedroom;
    }

    public void setBedroom(Integer bedroom) {
        this.bedroom = bedroom;
    }

    public Integer getBathroom() {
        return bathroom;
    }

    public void setBathroom(Integer bathroom) {
        this.bathroom = bathroom;
    }

//    public List<String> getImages() {
//        return Images;
//    }
//
//    public void setImages(List<String> images) {
//        Images = images;
//    }
}
