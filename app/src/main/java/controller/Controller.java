package controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }

}
