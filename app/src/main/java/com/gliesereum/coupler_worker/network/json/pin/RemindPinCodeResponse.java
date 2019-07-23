package com.gliesereum.coupler_worker.network.json.pin;

import com.google.gson.annotations.SerializedName;

public class RemindPinCodeResponse {
    @SerializedName("result")
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
