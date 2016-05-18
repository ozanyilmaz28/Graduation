package com.graduationteam.graduation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import adapters.SpinnerAdapter;
import entities.KeyCodes;
import entities.UserInfo;

public class SettingsActivity extends AppCompatActivity {


    Intent newPage_;
    Button logOut, update;
    EditText userName_, nameSurname_, phone_, mail_;
    SpinnerAdapter adapter;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();

        logOut = (Button) findViewById(R.id.btnSettingsLogOut);
        update = (Button) findViewById(R.id.btnSettingsUpdate);
        userName_ = (EditText) findViewById(R.id.SettingsUserName);
        nameSurname_ = (EditText) findViewById(R.id.SettingsNameSurname);
        phone_ = (EditText) findViewById(R.id.SettingsPhoneNumber);
        mail_ = (EditText) findViewById(R.id.SettingsEmail);

        userName_.setText(UserInfo.UserName);
        nameSurname_.setText(UserInfo.NameSurname);
        phone_.setText(UserInfo.Phone);
        mail_.setText(UserInfo.Email);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                editor = sharedPreferences.edit();
                editor.putBoolean("IsUserLoggedIn", false);
                editor.putLong("UserID", 0);
                editor.putString("UserName", "");
                editor.putString("NameSurname", "");
                editor.putString("Email", "");
                editor.putString("Phone", "");
                editor.putString("Password", "");
                editor.commit();

                finish();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPage_ = new Intent(SettingsActivity.this, SignUpActivity.class);
                UserInfo.SelectedPage = KeyCodes.SettingsToSingUp;
                startActivity(newPage_);
                finish();
            }
        });


    }
}
