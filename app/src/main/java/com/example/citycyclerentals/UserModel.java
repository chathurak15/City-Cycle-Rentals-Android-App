package com.example.citycyclerentals;

public class UserModel {
    private int userId;
    private String name;
    private String number;
    private String email;
    private String nic;
    private String dob;
    private String gender;
    private byte[] image;
    public UserModel() {
    }
    public UserModel(int userId, String name, String number, String email, String nic, String dob, String gender, byte[] image) {
        this.userId = userId;
        this.name = name;
        this.number = number;
        this.email = email;
        this.nic = nic;
        this.dob = dob;
        this.gender = gender;
        this.image = image;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
