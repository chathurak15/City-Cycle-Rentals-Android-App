package com.example.citycyclerentals;

public class StationModel {
    private int stationId;
    private String location;

    public StationModel(int stationId, String location) {
        this.stationId = stationId;
        this.location = location;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
