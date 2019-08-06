package com.gliesereum.coupler_worker.network.json.worker_new;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Rating {

    @SerializedName("rating")
    private double rating;

    @SerializedName("count")
    private double count;

    public void setRating(int rating) {
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getCount() {
        return count;
    }
}