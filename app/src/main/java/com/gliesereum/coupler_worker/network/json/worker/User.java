package com.gliesereum.coupler_worker.network.json.worker;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class User {

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("country")
    private String country;

    @SerializedName("corporationIds")
    private List<String> corporationIds;

    @SerializedName("address")
    private String address;

    @SerializedName("gender")
    private Object gender;

    @SerializedName("city")
    private String city;

    @SerializedName("avatarUrl")
    private String avatarUrl;

    @SerializedName("banStatus")
    private String banStatus;

    @SerializedName("lastSignIn")
    private long lastSignIn;

    @SerializedName("addAddress")
    private String addAddress;

    @SerializedName("kycApproved")
    private Object kycApproved;

    @SerializedName("coverUrl")
    private Object coverUrl;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("phone")
    private String phone;

    @SerializedName("middleName")
    private String middleName;

    @SerializedName("lastActivity")
    private long lastActivity;

    @SerializedName("id")
    private String id;

    @SerializedName("createDate")
    private long createDate;

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

    public void setCorporationIds(List<String> corporationIds) {
        this.corporationIds = corporationIds;
    }

    public List<String> getCorporationIds() {
        return corporationIds;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public Object getGender() {
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

    public void setLastSignIn(long lastSignIn) {
        this.lastSignIn = lastSignIn;
    }

    public long getLastSignIn() {
        return lastSignIn;
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

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setLastActivity(long lastActivity) {
        this.lastActivity = lastActivity;
    }

    public long getLastActivity() {
        return lastActivity;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getCreateDate() {
        return createDate;
    }
}