package com.gliesereum.coupler_worker.network.json.client_record_new;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class ContentItem {

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("canceledDescription")
    private Object canceledDescription;

    @SerializedName("packageDto")
    private PackageDto packageDto;

    @SerializedName("businessId")
    private String businessId;

    @SerializedName("description")
    private String description;

    @SerializedName("statusPay")
    private String statusPay;

    @SerializedName("payType")
    private String payType;

    @SerializedName("workingSpaceId")
    private String workingSpaceId;

    @SerializedName("price")
    private int price;

    @SerializedName("client")
    private Client client;

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
    private Business business;

    @SerializedName("statusRecord")
    private String statusRecord;

    @SerializedName("servicesIds")
    private List<Object> servicesIds;

    @SerializedName("packageId")
    private String packageId;

    @SerializedName("services")
    private List<ServicesItem> services;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("phone")
    private String phone;

    @SerializedName("businessCategoryId")
    private String businessCategoryId;

    @SerializedName("middleName")
    private String middleName;

    @SerializedName("begin")
    private long begin;

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setCanceledDescription(Object canceledDescription) {
        this.canceledDescription = canceledDescription;
    }

    public Object getCanceledDescription() {
        return canceledDescription;
    }

    public void setPackageDto(PackageDto packageDto) {
        this.packageDto = packageDto;
    }

    public PackageDto getPackageDto() {
        return packageDto;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public void setBusiness(Business business) {
        this.business = business;
    }

    public Business getBusiness() {
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

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setServices(List<ServicesItem> services) {
        this.services = services;
    }

    public List<ServicesItem> getServices() {
        return services;
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

    public void setBusinessCategoryId(String businessCategoryId) {
        this.businessCategoryId = businessCategoryId;
    }

    public String getBusinessCategoryId() {
        return businessCategoryId;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getBegin() {
        return begin;
    }
}