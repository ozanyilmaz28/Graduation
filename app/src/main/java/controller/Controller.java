package controller;

import android.content.Context;
import android.content.SharedPreferences;

import entities.UserInfo;

/**
 * Created by LA-173 on 24.03.2016.
 */
public class Controller {

    public boolean isUserLoggedIn(Context con_) {

        SharedPreferences pref = con_.getSharedPreferences("LoggedUserInformation", 0);
        SharedPreferences.Editor editor_ = pref.edit();

        if (pref.getBoolean("IsUserLoggedIn", false) != false) {

            UserInfo info_ = new UserInfo();
            info_.setUserID(pref.getInt("LoggedUserID", 0));
            info_.setUserName(pref.getString("LoggedUserName", ""));
            info_.setEmail(pref.getString("LoggedEmail", ""));
            info_.setPhone(pref.getString("LoggedPhone", ""));

            return true;
        } else
            return false;

    }

}
