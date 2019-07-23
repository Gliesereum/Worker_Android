package com.gliesereum.coupler_worker.network.json.car;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Year {

    @SerializedName("name")
    private int name;

    @SerializedName("id")
    private String id;

    public void setName(int name) {
        this.name = name;
    }

    public int getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}