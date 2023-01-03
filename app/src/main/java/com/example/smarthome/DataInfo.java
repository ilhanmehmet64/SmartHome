package com.example.smarthome;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataInfo {
    @SerializedName("distance")
    @Expose
    private int distance;

    @SerializedName("humidity")
    @Expose
    private int humidity;

    @SerializedName("temperature")
    @Expose
    private float temperature;

    @SerializedName("led")
    @Expose
    private boolean led;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public boolean getLed() {
        return led;
    }

    public void setLed(boolean led) {
        this.led = led;
    }
}
