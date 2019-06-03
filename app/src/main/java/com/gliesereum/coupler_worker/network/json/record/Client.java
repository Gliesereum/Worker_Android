package com.gliesereum.coupler_worker.network.json.record;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Client {

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("country")
    private String country;

    @SerializedName("corporationIds")
    private List<Object> corporationIds;

    @SerializedName("address")
    private String address;

    @SerializedName("gender")
    private String gender;

    @SerializedName("city")
    private String city;

    @SerializedName("avatarUrl")
    private String avatarUrl;

    @SerializedName("banStatus")
    private String banStatus;

    @SerializedName("addAddress")
    private String addAddress;

    @SerializedName("kycApproved")
    private Object kycApproved;

    @SerializedName("coverUrl")
    private Object coverUrl;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("middleName")
    private String middleName;

    @SerializedName("id")
    private String id;

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setCorporationIds(List<Object> corporationIds) {
        this.corporationIds = corporationIds;
    }

    public List<Object> getCorporationIds() {
        return corporationIds;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setBanStatus(String banStatus) {
        this.banStatus = banStatus;
    }

    public String getBanStatus() {
        return banStatus;
    }

    public void setAddAddress(String addAddress) {
        this.addAddress = addAddress;
    }

    public String getAddAddress() {
        return addAddress;
    }

    public void setKycApproved(Object kycApproved) {
        this.kycApproved = kycApproved;
    }

    public Object getKycApproved() {
        return kycApproved;
    }

    public void setCoverUrl(Object coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Object getCoverUrl() {
        return coverUrl;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}