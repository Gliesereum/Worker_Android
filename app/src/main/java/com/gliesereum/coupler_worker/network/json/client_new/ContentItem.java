package com.gliesereum.coupler_worker.network.json.client_new;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ContentItem {

    @SerializedName("corporationIds")
    private List<String> corporationIds;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("clientId")
    private String clientId;

    @SerializedName("phone")
    private String phone;

    @SerializedName("avatarUrl")
    private String avatarUrl;

    @SerializedName("middleName")
    private String middleName;

    @SerializedName("id")
    private String id;

    @SerializedName("email")
    private Object email;

    @SerializedName("businessIds")
    private List<String> businessIds;

    public void setCorporationIds(List<String> corporationIds) {
        this.corporationIds = corporationIds;
    }

    public List<String> getCorporationIds() {
        return corporationIds;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
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

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getEmail() {
        return email;
    }

    public void setBusinessIds(List<String> businessIds) {
        this.businessIds = businessIds;
    }

    public List<String> getBusinessIds() {
        return businessIds;
    }
}