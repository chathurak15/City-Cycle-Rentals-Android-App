package com.example.citycyclerentals;

import android.os.Parcel;
import android.os.Parcelable;

public class BikeModel implements Parcelable{
    private int bikeId;
    private String name;
    private String type;
    private int status;
    private double price;
    private double rating;
    private String location;
    private int image;

    public BikeModel() {
    }
    public BikeModel(int bikeId, String name, String type, int status, double price, double rating, String location, int image) {
        this.bikeId = bikeId;
        this.name = name;
        this.type = type;
        this.status = status;
        this.price = price;
        this.rating = rating;
        this.location = location;
        this.image = image;
    }

    public int getBikeId() {
        return bikeId;
    }

    public void setBikeId(int bikeId) {
        this.bikeId = bikeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    protected BikeModel(Parcel in) {
        name = in.readString();
        price = in.readDouble();
        type = in.readString();
        location = in.readString();
        image = in.readInt();
        status = in.readInt();
    }

    public static final Parcelable.Creator<BikeModel> CREATOR = new Parcelable.Creator<BikeModel>() {
        @Override
        public BikeModel createFromParcel(Parcel in) {
            return new BikeModel(in);
        }

        @Override
        public BikeModel[] newArray(int size) {
            return new BikeModel[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeString(type);
        dest.writeString(location);
        dest.writeInt(image);
        dest.writeInt(status);
    }
}