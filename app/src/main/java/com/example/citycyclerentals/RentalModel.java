package com.example.citycyclerentals;

public class RentalModel {
    private int rentalId;
    private int userId;
    private int bikeId;
    private double amount;
    private String status;
    private String startTime;
    private String endTime;
    private String pickupStation;
    private int returnStationId;
    private String returnStation;

    public RentalModel(int rentalId, int userId, int bikeId, double amount, String status, String startTime, String endTime, String pickupStation, int returnStationId, String returnStation) {
        this.rentalId = rentalId;
        this.userId = userId;
        this.bikeId = bikeId;
        this.amount = amount;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.pickupStation = pickupStation;
        this.returnStationId = returnStationId;
        this.returnStation = returnStation;
    }

    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBikeId() {
        return bikeId;
    }

    public void setBikeId(int bikeId) {
        this.bikeId = bikeId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPickupStation() {
        return pickupStation;
    }

    public void setPickupStation(String pickupStation) {
        this.pickupStation = pickupStation;
    }

    public int getReturnStationId() {
        return returnStationId;
    }

    public void setReturnStationId(int returnStationId) {
        this.returnStationId = returnStationId;
    }

    public String getReturnStation() {
        return returnStation;
    }

    public void setReturnStation(String returnStation) {
        this.returnStation = returnStation;
    }
}
