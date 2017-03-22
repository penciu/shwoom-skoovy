package com.skoovy.android;

/**
 * Created by Rudi Wever on 3/19/2017.
 */

public class SkoovyUser {
    private String skoovyUserName;
    private String skoovyUserPassword;

    public SkoovyUser() {

    }

    public SkoovyUser(String userName, String userPassword){
        this.skoovyUserName = userName;
        this.skoovyUserPassword = userPassword;
    }

    public String getSkoovyUserName() {
        return skoovyUserName;
    }

    public String getSkoovyUserPassword() {
        return skoovyUserPassword;
    }
}
