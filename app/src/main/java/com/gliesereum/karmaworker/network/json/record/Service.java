package com.gliesereum.karmaworker.network.json.record;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Service {

    @SerializedName("objectState")
    private String objectState;

    @SerializedName("name")
    private String name;

    @SerializedName("businessCategoryId")
    private String businessCategoryId;

    @SerializedName("description")
    private String description;

    @SerializedName("id")
    private String id;

    public void setObjectState(String objectState) {
        this.objectState = objectState;
    }

    public String getObjectState() {
        return objectState;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setBusinessCategoryId(String businessCategoryId) {
        this.businessCategoryId = businessCategoryId;
    }

    public String getBusinessCategoryId() {
        return businessCategoryId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}