package com.gliesereum.coupler_worker.network.json.record;

import com.google.gson.annotations.SerializedName;

public class PaymentResponse {

    @SerializedName("sum")
    private Integer sum;

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }
}
