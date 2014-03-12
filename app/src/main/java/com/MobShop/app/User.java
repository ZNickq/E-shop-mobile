package com.MobShop.app;

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

    User(){

    }

    User(String e){
        this.email = e;
    }

    public void setLoggedIn(boolean val){
        this.loggedIn = val;
    }

    public void setName(String n){
        this.name = n;
    }

    public void setSurname(String sn){
        this.surname = sn;
    }

    public void setCity(String c){
        this.city = c;
    }

    public void setDistrict(String d){
        this.district = d;
    }

    public void setAddress(String a){
        this.address = a;
    }

    public void setPhoneNumber(String ph){
        this.phoneNumber = ph;
    }

    public boolean getLoggedIn(){
        return loggedIn;
    }

    public String getEmail(){
        return this.email;
    }

    public String getName(){
        return this.name;
    }

    public String getSurname(){
        return this.surname;
    }

    public String getCity(){
        return this.city;
    }

    public String getDistrict(){
        return this.district;
    }

    public String getAddress(){
        return this.address;
    }

    public String getPhoneNumber(){
        return this.phoneNumber;
    }


}
