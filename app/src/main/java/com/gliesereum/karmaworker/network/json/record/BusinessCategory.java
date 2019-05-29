package com.gliesereum.karmaworker.network.json.record;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class BusinessCategory {

    @SerializedName("code")
    private String code;

    @SerializedName("imageUrl")
    private Object imageUrl;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("active")
    private boolean active;

    @SerializedName("orderIndex")
    private Object orderIndex;

    @SerializedName("id")
    private String id;

    @SerializedName("businessType")
    private String businessType;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setImageUrl(Object imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Object getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setOrderIndex(Object orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Object getOrderIndex() {
        return orderIndex;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessType() {
        return businessType;
    }
}