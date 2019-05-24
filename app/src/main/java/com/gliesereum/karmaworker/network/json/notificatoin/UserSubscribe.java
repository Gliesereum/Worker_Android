package com.gliesereum.karmaworker.network.json.notificatoin;

import com.google.gson.annotations.SerializedName;

public class UserSubscribe {

    @SerializedName("userDeviceId")
    private String userDeviceId;

    @SerializedName("notificationEnable")
    private Boolean notificationEnable;

    @SerializedName("objectId")
    private String objectId;

    @SerializedName("subscribeDestination")
    private String subscribeDestination;

    public UserSubscribe(Boolean notificationEnable, String subscribeDestination) {
        this.notificationEnable = notificationEnable;
        this.subscribeDestination = subscribeDestination;
    }

    public String getUserDeviceId() {
        return userDeviceId;
    }

    public void setUserDeviceId(String userDeviceId) {
        this.userDeviceId = userDeviceId;
    }

    public Boolean getNotificationEnable() {
        return notificationEnable;
    }

    public void setNotificationEnable(Boolean notificationEnable) {
        this.notificationEnable = notificationEnable;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getSubscribeDestination() {
        return subscribeDestination;
    }

    public void setSubscribeDestination(String subscribeDestination) {
        this.subscribeDestination = subscribeDestination;
    }
}
