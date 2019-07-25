package com.gliesereum.coupler_worker.network.json.pin;

import com.google.gson.annotations.SerializedName;

public class RemindPinCodeResponse {
    @SerializedName("result")
    private Boolean result;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}
