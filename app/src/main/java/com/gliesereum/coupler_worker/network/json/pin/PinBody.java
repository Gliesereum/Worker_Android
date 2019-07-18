package com.gliesereum.coupler_worker.network.json.pin;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class PinBody {

    @SerializedName("pinCode")
    private String pinCode;

    @SerializedName("id")
    private String id;

    @SerializedName("userId")
    private String userId;

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}