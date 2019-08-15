package com.gliesereum.coupler_worker.network.json.client_new;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class NewClientResponse {

    @SerializedName("number")
    private int number;

    @SerializedName("numberOfElements")
    private int numberOfElements;

    @SerializedName("last")
    private boolean last;

    @SerializedName("size")
    private int size;

    @SerializedName("totalPages")
    private int totalPages;

    @SerializedName("pageable")
    private Pageable pageable;

    @SerializedName("sort")
    private Sort sort;

    @SerializedName("content")
    private List<ClientItem> content;

    @SerializedName("first")
    private boolean first;

    @SerializedName("scrollId")
    private Object scrollId;

    @SerializedName("totalElements")
    private int totalElements;

    @SerializedName("empty")
    private boolean empty;

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public boolean isLast() {
        return last;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public Sort getSort() {
        return sort;
    }

    public void setContent(List<ClientItem> content) {
        this.content = content;
    }

    public List<ClientItem> getContent() {
        return content;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isFirst() {
        return first;
    }

    public void setScrollId(Object scrollId) {
        this.scrollId = scrollId;
    }

    public Object getScrollId() {
        return scrollId;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}