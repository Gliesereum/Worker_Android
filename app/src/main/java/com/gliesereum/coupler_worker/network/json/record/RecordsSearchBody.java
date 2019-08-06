package com.gliesereum.coupler_worker.network.json.record;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecordsSearchBody {

    @SerializedName("status")
    private List<String> status;

    @SerializedName("processes")
    private List<String> processes;

    @SerializedName("workingSpaceIds")
    private List<String> workingSpaceIds;

    @SerializedName("targetIds")
    private List<String> targetIds;

//    @SerializedName("businessCategoryId")
//    private String businessCategoryId;

    @SerializedName("from")
    private Long from;

    @SerializedName("to")
    private Long to;

    @SerializedName("corporationId")
    private String corporationId;

    @SerializedName("businessIds")
    private List<String> businessIds;

    @SerializedName("size")
    private Integer size;

    @SerializedName("page")
    private Integer page;

    public String getCorporationId() {
        return corporationId;
    }

    public void setCorporationId(String corporationId) {
        this.corporationId = corporationId;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public List<String> getProcesses() {
        return processes;
    }

    public void setProcesses(List<String> processes) {
        this.processes = processes;
    }

    public List<String> getWorkingSpaceIds() {
        return workingSpaceIds;
    }

    public void setWorkingSpaceIds(List<String> workingSpaceIds) {
        this.workingSpaceIds = workingSpaceIds;
    }

    public List<String> getTargetIds() {
        return targetIds;
    }

    public void setTargetIds(List<String> targetIds) {
        this.targetIds = targetIds;
    }

//    public String getBusinessCategoryId() {
//        return businessCategoryId;
//    }
//
//    public void setBusinessCategoryId(String businessCategoryId) {
//        this.businessCategoryId = businessCategoryId;
//    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public List<String> getBusinessIds() {
        return businessIds;
    }

    public void setBusinessIds(List<String> businessIds) {
        this.businessIds = businessIds;
    }
}
