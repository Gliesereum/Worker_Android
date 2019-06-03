package com.gliesereum.coupler_worker.network.json.record;

import com.gliesereum.coupler_worker.network.json.carwash.PackagesItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class AllRecordResponse {

	@SerializedName("statusProcess")
	private String statusProcess;

	@SerializedName("workerId")
	private String workerId;

	@SerializedName("clientId")
	private String clientId;

	@SerializedName("targetId")
	private String targetId;

	@SerializedName("business")
	private Business business;

	@SerializedName("packageDto")
	private PackagesItem packageDto;

	@SerializedName("statusRecord")
	private String statusRecord;

	@SerializedName("servicesIds")
	private List<Object> servicesIds;

	@SerializedName("packageId")
	private Object packageId;

	@SerializedName("businessId")
	private String businessId;

	@SerializedName("description")
	private String description;

	@SerializedName("services")
	private List<ServicesItem> services;

	@SerializedName("statusPay")
	private String statusPay;

	@SerializedName("payType")
	private String payType;

	@SerializedName("workingSpaceId")
	private String workingSpaceId;

	@SerializedName("price")
	private int price;

	@SerializedName("businessCategoryId")
	private String businessCategoryId;

	@SerializedName("client")
	private Client client;

	@SerializedName("finish")
	private long finish;

	@SerializedName("id")
	private String id;

	@SerializedName("begin")
	private long begin;

	@SerializedName("notificationSend")
	private boolean notificationSend;

	public void setStatusProcess(String statusProcess) {
		this.statusProcess = statusProcess;
	}

	public String getStatusProcess() {
		return statusProcess;
	}

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}

	public String getWorkerId() {
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

	public PackagesItem getPackageDto() {
		return packageDto;
	}

	public void setPackageDto(PackagesItem packageDto) {
		this.packageDto = packageDto;
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

	public void setServices(List<ServicesItem> services) {
		this.services = services;
	}

	public List<ServicesItem> getServices() {
		return services;
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

	public void setBusinessCategoryId(String businessCategoryId) {
		this.businessCategoryId = businessCategoryId;
	}

	public String getBusinessCategoryId() {
		return businessCategoryId;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Client getClient() {
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

	public void setBegin(long begin) {
		this.begin = begin;
	}

	public long getBegin() {
		return begin;
	}

	public void setNotificationSend(boolean notificationSend) {
		this.notificationSend = notificationSend;
	}

	public boolean isNotificationSend() {
		return notificationSend;
	}
}