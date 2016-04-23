package com.graduationteam.graduation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    Button logOut;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        logOut = (Button) findViewById(R.id.btnSettingsLogOut);


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
                editor.commit();

                finish();
            }
        });
    }
}
