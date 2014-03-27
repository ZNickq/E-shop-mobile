package com.MobShop.app.models;

/**
 * Created by Segarceanu Calin on 3/10/14.
 */
public class User {
    public static boolean loggedIn = false;
    private static String email;
    private static String name;
    private static String surname;
    private static String city;
    private static String district;
    private static String address;
    private static String phoneNumber;

    public User() {

    }

    public User(String e) {
        this.email = e;
    }

    public boolean getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean val) {
        this.loggedIn = val;
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
