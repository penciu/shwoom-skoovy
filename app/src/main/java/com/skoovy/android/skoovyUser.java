package com.skoovy.android;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by Rudi Wever on 3/19/2017.
 * Class holds profile stats for logged in Skoovy user.
 */

public class SkoovyUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private int skoovyUserPoints;
    private int skoovyUserPosts;
    private int skoovyUserFollowers;
    private int skoovyUserFollowing;

    public SkoovyUser() {
        Log.d("User", "default SkoovyUser constructor used");
    }

    public SkoovyUser(int skoovyPoints, int skoovyPosts, int skoovyFollowers, int skoovyFollowing){
        Log.d("User", "Standard SkoovyUser constructor used");

        this.skoovyUserPoints = skoovyPoints;
        this.skoovyUserPosts = skoovyPosts;
        this.skoovyUserFollowers = skoovyFollowers;
        this.skoovyUserFollowing = skoovyFollowing;
    }

    public int getSkoovyUserPoints() {
        return skoovyUserPoints;
    }

    public int getSkoovyUserPosts() {
        return skoovyUserPosts;
    }

    public int getSkoovyUserFollowers() {
        return skoovyUserFollowers;
    }

    public int getSkoovyUserFollowing() {
        return skoovyUserFollowing;
    }

    public void setSkoovyUserPoints(int skoovyUserPoints) {
        this.skoovyUserPoints = skoovyUserPoints;
    }

    public void setSkoovyUserPosts(int skoovyUserPosts) {
        this.skoovyUserPosts = skoovyUserPosts;
    }

    public void setSkoovyUserFollowers(int skoovyUserFollowers) {
        this.skoovyUserFollowers = skoovyUserFollowers;
        Log.d("User", "SKOOVYUSERFOLLOWERS WAS SET");
        Log.d("User", "SkoovyUser Class shows "+ getSkoovyUserFollowers()+" followers for this user.");
    }

    public void setSkoovyUserFollowing(int skoovyUserFollowing) {
        this.skoovyUserFollowing = skoovyUserFollowing;
    }

    @Override
    public String toString() {
        return "SkoovyUser skoovyuser [points=" + skoovyUserPoints + ", posts=" + skoovyUserPosts + ", followers=" + skoovyUserFollowers + ", following=" + skoovyUserFollowing + "]";
    }

}
