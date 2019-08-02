package com.gliesereum.coupler_worker.network.json.order;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class OrderBody {

    @SerializedName("workerId")
    private String workerId;

    @SerializedName("specifiedWorkingSpace")
    private boolean specifiedWorkingSpace;

    @SerializedName("clientId")
    private String clientId;

    @SerializedName("workingSpaceId")
    private String workingSpaceId;

    @SerializedName("servicesIds")
    private List<String> servicesIds;

    @SerializedName("description")
    private String description;

    @SerializedName("begin")
    private long begin;

    @SerializedName("targetId")
    private String targetId;

    @SerializedName("businessId")
    private String businessId;

    @SerializedName("packageId")
    private String packageId;

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public boolean isSpecifiedWorkingSpace() {
        return specifiedWorkingSpace;
    }

    public void setSpecifiedWorkingSpace(boolean specifiedWorkingSpace) {
        this.specifiedWorkingSpace = specifiedWorkingSpace;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public void setWorkingSpaceId(String workingSpaceId) {
        this.workingSpaceId = workingSpaceId;
    }

    public String getWorkingSpaceId() {
        return workingSpaceId;
    }

    public void setServicesIds(List<String> servicesIds) {
        this.servicesIds = servicesIds;
    }

    public List<String> getServicesIds() {
        return servicesIds;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getBegin() {
        return begin;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
}