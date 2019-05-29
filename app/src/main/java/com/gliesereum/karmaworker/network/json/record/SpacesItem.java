package com.gliesereum.karmaworker.network.json.record;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class SpacesItem {

    @SerializedName("statusSpace")
    private String statusSpace;

    @SerializedName("name")
    private String name;

    @SerializedName("businessId")
    private String businessId;

    @SerializedName("businessCategoryId")
    private String businessCategoryId;

    @SerializedName("description")
    private Object description;

    @SerializedName("id")
    private String id;

    @SerializedName("indexNumber")
    private int indexNumber;

    @SerializedName("workers")
    private List<Object> workers;

    public void setStatusSpace(String statusSpace) {
        this.statusSpace = statusSpace;
    }

    public String getStatusSpace() {
        return statusSpace;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessCategoryId(String businessCategoryId) {
        this.businessCategoryId = businessCategoryId;
    }

    public String getBusinessCategoryId() {
        return businessCategoryId;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Object getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setIndexNumber(int indexNumber) {
        this.indexNumber = indexNumber;
    }

    public int getIndexNumber() {
        return indexNumber;
    }

    public void setWorkers(List<Object> workers) {
        this.workers = workers;
    }

    public List<Object> getWorkers() {
        return workers;
    }
}