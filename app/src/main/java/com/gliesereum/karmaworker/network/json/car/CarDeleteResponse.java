package com.gliesereum.karmaworker.network.json.car;

import com.google.gson.annotations.SerializedName;

public class CarDeleteResponse {

    @SerializedName("result")
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
