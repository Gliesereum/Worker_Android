package com.gliesereum.coupler_worker.network.json.carwash;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class RecordsItem {

    @SerializedName("canceledDescription")
    private Object canceledDescription;

    @SerializedName("packageDto")
    private Object packageDto;

    @SerializedName("businessId")
    private String businessId;

    @SerializedName("description")
    private Object description;

    @SerializedName("statusPay")
    private String statusPay;

    @SerializedName("payType")
    private String payType;

    @SerializedName("workingSpaceId")
    private String workingSpaceId;

    @SerializedName("price")
    private int price;

    @SerializedName("client")
    private Object client;

    @SerializedName("finish")
    private long finish;

    @SerializedName("id")
    private String id;

    @SerializedName("notificationSend")
    private boolean notificationSend;

    @SerializedName("statusProcess")
    private String statusProcess;

    @SerializedName("recordNumber")
    private int recordNumber;

    @SerializedName("workerId")
    private Object workerId;

    @SerializedName("clientId")
    private String clientId;

    @SerializedName("targetId")
    private String targetId;

    @SerializedName("business")
    private Object business;

    @SerializedName("statusRecord")
    private String statusRecord;

    @SerializedName("servicesIds")
    private List<Object> servicesIds;

    @SerializedName("packageId")
    private Object packageId;

    @SerializedName("services")
    private List<ServicesItem> services;

    @SerializedName("specifiedWorkingSpace")
    private boolean specifiedWorkingSpace;

    @SerializedName("businessCategoryId")
    private String businessCategoryId;

    @SerializedName("begin")
    private long begin;

    public void setCanceledDescription(Object canceledDescription) {
        this.canceledDescription = canceledDescription;
    }

    public Object getCanceledDescription() {
        return canceledDescription;
    }

    public void setPackageDto(Object packageDto) {
        this.packageDto = packageDto;
    }

    public Object getPackageDto() {
        return packageDto;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Object getDescription() {
        return description;
    }

    public void setStatusPay(String statusPay) {
        this.statusPay = statusPay;
    }

    public String getStatusPay() {
        return statusPay;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayType() {
        return payType;
    }

    public void setWorkingSpaceId(String workingSpaceId) {
        this.workingSpaceId = workingSpaceId;
    }

    public String getWorkingSpaceId() {
        return workingSpaceId;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setClient(Object client) {
        this.client = client;
    }

    public Object getClient() {
        return client;
    }

    public void setFinish(long finish) {
        this.finish = finish;
    }

    public long getFinish() {
        return finish;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setNotificationSend(boolean notificationSend) {
        this.notificationSend = notificationSend;
    }

    public boolean isNotificationSend() {
        return notificationSend;
    }

    public void setStatusProcess(String statusProcess) {
        this.statusProcess = statusProcess;
    }

    public String getStatusProcess() {
        return statusProcess;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setWorkerId(Object workerId) {
        this.workerId = workerId;
    }

    public Object getWorkerId() {
        return workerId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setBusiness(Object business) {
        this.business = business;
    }

    public Object getBusiness() {
        return business;
    }

    public void setStatusRecord(String statusRecord) {
        this.statusRecord = statusRecord;
    }

    public String getStatusRecord() {
        return statusRecord;
    }

    public void setServicesIds(List<Object> servicesIds) {
        this.servicesIds = servicesIds;
    }

    public List<Object> getServicesIds() {
        return servicesIds;
    }

    public void setPackageId(Object packageId) {
        this.packageId = packageId;
    }

    public Object getPackageId() {
        return packageId;
    }

    public void setServices(List<ServicesItem> services) {
        this.services = services;
    }

    public List<ServicesItem> getServices() {
        return services;
    }

    public void setSpecifiedWorkingSpace(boolean specifiedWorkingSpace) {
        this.specifiedWorkingSpace = specifiedWorkingSpace;
    }

    public boolean isSpecifiedWorkingSpace() {
        return specifiedWorkingSpace;
    }

    public void setBusinessCategoryId(String businessCategoryId) {
        this.businessCategoryId = businessCategoryId;
    }

    public String getBusinessCategoryId() {
        return businessCategoryId;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getBegin() {
        return begin;
    }
}