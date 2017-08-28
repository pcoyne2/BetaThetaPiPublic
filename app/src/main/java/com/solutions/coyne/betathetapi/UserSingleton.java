package com.solutions.coyne.betathetapi;

/**
 * Created by Patrick Coyne on 8/25/2017.
 */

public class UserSingleton {

    User user;
    private static UserSingleton instance;

    private UserSingleton() {
    }

    public static UserSingleton getInstance(){
        if(instance == null){
            instance = new UserSingleton();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
