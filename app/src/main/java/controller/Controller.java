package controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import entities.UserInfo;

/**
 * Created by LA-173 on 24.03.2016.
 */
public class Controller {

    public boolean isUserLoggedIn(Context con_) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(con_);
        SharedPreferences.Editor editor_ = pref.edit();

        if (pref.getBoolean("IsUserLoggedIn", false) != false) {

            UserInfo.UserID = pref.getLong("UserID", 0);
            UserInfo.UserName = pref.getString("UserName", "");
            UserInfo.NameSurname = pref.getString("NameSurname", "");
            UserInfo.Email = pref.getString("Email", "");
            UserInfo.Phone = pref.getString("Phone", "");

            return true;
        } else
            return false;

    }

}
