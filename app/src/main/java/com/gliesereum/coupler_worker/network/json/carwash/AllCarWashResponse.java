package com.gliesereum.coupler_worker.network.json.carwash;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class AllCarWashResponse {

    @SerializedName("updateDate")
    private long updateDate;

    @SerializedName("corporationId")
    private String corporationId;

    @SerializedName("servicePrices")
    private List<ServicePricesItem> servicePrices;

    @SerializedName("records")
    private List<Object> records;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("businessId")
    private String businessId;

    @SerializedName("rating")
    private Rating rating;

    @SerializedName("description")
    private String description;

    @SerializedName("media")
    private List<Object> media;

    @SerializedName("descriptions")
    private List<Object> descriptions;

    @SerializedName("workTimes")
    private List<WorkTimesItem> workTimes;

    @SerializedName("id")
    private String id;

    @SerializedName("workers")
    private List<WorkersItem> workers;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("createDate")
    private long createDate;

    @SerializedName("address")
    private String address;

    @SerializedName("comments")
    private List<CommentsItem> comments;

    @SerializedName("objectState")
    private String objectState;

    @SerializedName("timeZone")
    private int timeZone;

    @SerializedName("packages")
    private List<PackagesItem> packages;

    @SerializedName("logoUrl")
    private String logoUrl;

    @SerializedName("addPhone")
    private Object addPhone;

    @SerializedName("phone")
    private String phone;

    @SerializedName("name")
    private String name;

    @SerializedName("businessCategoryId")
    private String businessCategoryId;

    @SerializedName("businessCategory")
    private BusinessCategory businessCategory;

    @SerializedName("spaces")
    private List<SpacesItem> spaces;

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setCorporationId(String corporationId) {
        this.corporationId = corporationId;
    }

    public String getCorporationId() {
        return corporationId;
    }

    public void setServicePrices(List<ServicePricesItem> servicePrices) {
        this.servicePrices = servicePrices;
    }

    public List<ServicePricesItem> getServicePrices() {
        return servicePrices;
    }

    public void setRecords(List<Object> records) {
        this.records = records;
    }

    public List<Object> getRecords() {
        return records;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Rating getRating() {
        return rating;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setMedia(List<Object> media) {
        this.media = media;
    }

    public List<Object> getMedia() {
        return media;
    }

    public void setDescriptions(List<Object> descriptions) {
        this.descriptions = descriptions;
    }

    public List<Object> getDescriptions() {
        return descriptions;
    }

    public void setWorkTimes(List<WorkTimesItem> workTimes) {
        this.workTimes = workTimes;
    }

    public List<WorkTimesItem> getWorkTimes() {
        return workTimes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setWorkers(List<WorkersItem> workers) {
        this.workers = workers;
    }

    public List<WorkersItem> getWorkers() {
        return workers;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setComments(List<CommentsItem> comments) {
        this.comments = comments;
    }

    public List<CommentsItem> getComments() {
        return comments;
    }

    public void setObjectState(String objectState) {
        this.objectState = objectState;
    }

    public String getObjectState() {
        return objectState;
    }

    public void setTimeZone(int timeZone) {
        this.timeZone = timeZone;
    }

    public int getTimeZone() {
        return timeZone;
    }

    public void setPackages(List<PackagesItem> packages) {
        this.packages = packages;
    }

    public List<PackagesItem> getPackages() {
        return packages;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setAddPhone(Object addPhone) {
        this.addPhone = addPhone;
    }

    public Object getAddPhone() {
        return addPhone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
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

    public void setBusinessCategory(BusinessCategory businessCategory) {
        this.businessCategory = businessCategory;
    }

    public BusinessCategory getBusinessCategory() {
        return businessCategory;
    }

    public void setSpaces(List<SpacesItem> spaces) {
        this.spaces = spaces;
    }

    public List<SpacesItem> getSpaces() {
        return spaces;
    }
}