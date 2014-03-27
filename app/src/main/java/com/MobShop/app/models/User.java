package com.MobShop.app.models;

public class User {
    private boolean loggedIn;
    private String email;
    private String name;
    private String surname;
    private String city;
    private String district;
    private String address;
    private String phoneNumber;

    public User() {

    }

    public User(String e) {
        this.email = e;
    }

    public boolean getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String n) {
        this.name = n;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String sn) {
        this.surname = sn;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String c) {
        this.city = c;
    }

    public String getDistrict() {
        return this.district;
    }

    public void setDistrict(String d) {
        this.district = d;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String a) {
        this.address = a;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String ph) {
        this.phoneNumber = ph;
    }


}
