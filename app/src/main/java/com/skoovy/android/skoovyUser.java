package com.skoovy.android;

import android.util.Log;

/**
 * Created by Rudi Wever on 3/19/2017.
 * Class currently not used
 */

public class SkoovyUser {
    private String skoovyUserName;
    private String skoovyUserPassword;
    private String skoovyUserlastname;
    private String skoovyUserfirstname;
    private String skoovyUserbirthday;
    private String skoovyUseremail;

    public SkoovyUser() {
        Log.d("User", "default SkoovyUser constructor used");
    }


    public SkoovyUser(String lastname, String email, String userPassword, String firstname, String userName, String birthday){
        Log.d("User", "Standard SkoovyUser constructor used");

        this.skoovyUserName = userName;
        this.skoovyUserPassword = userPassword;
    }

    public String getSkoovyUserName() {
        return skoovyUserName;
    }

    public void setpassword(String password){
        this.skoovyUserPassword = password;
    }

    public String getSkoovyUserPassword() {
        return skoovyUserPassword;
    }

    @Override
    public String toString() {
        return "SkoovyUser skoovyuser [firstname=" + skoovyUserfirstname + ", lastname=" + skoovyUserlastname + ", birthday=" + skoovyUserbirthday + ", username=" + skoovyUserName + ", email=" + skoovyUseremail +  ", password=" + skoovyUserPassword + "]";
    }

}
