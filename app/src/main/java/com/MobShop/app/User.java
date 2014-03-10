package com.MobShop.app;

/**
 * Created by Segarceanu Calin on 3/10/14.
 */
public class User {
    public static boolean loggedIn = false;
    private  String email;

    User(){

    }

    User(String e){
        this.email = e;
    }

    public void setLoggedIn(boolean val){
        this.loggedIn = val;
    }

    public boolean getLoggedIn(){
        return loggedIn;
    }


}
