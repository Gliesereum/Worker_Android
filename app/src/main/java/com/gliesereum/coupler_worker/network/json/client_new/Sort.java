package com.gliesereum.coupler_worker.network.json.client_new;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Sort {

    @SerializedName("unsorted")
    private boolean unsorted;

    @SerializedName("sorted")
    private boolean sorted;

    @SerializedName("empty")
    private boolean empty;

    public void setUnsorted(boolean unsorted) {
        this.unsorted = unsorted;
    }

    public boolean isUnsorted() {
        return unsorted;
    }

    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    public boolean isSorted() {
        return sorted;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}