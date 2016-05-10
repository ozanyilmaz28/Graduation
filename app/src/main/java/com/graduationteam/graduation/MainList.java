package com.graduationteam.graduation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import adapters.ImageAdapter;
import controller.Controller;
import entities.KeyCodes;
import entities.UserInfo;

public class MainList extends AppCompatActivity {

    Button btnCategory, btnCreateAdvert, btnMyPage, btnSettings;
    GridView gridView;
    Intent intent;

    Controller controller_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        gridView = (GridView) findViewById(R.id.gridView);
        btnCategory = (Button) findViewById(R.id.btnCategory);
        btnCreateAdvert = (Button) findViewById(R.id.btnCreateAdvert);
        btnMyPage = (Button) findViewById(R.id.btnMyPage);
        btnSettings = (Button) findViewById(R.id.btnSettings);

        gridView.setAdapter(new ImageAdapter(this));

        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainList.this, CategoryListActivity.class);
                startActivity(intent);

            }
        });

        btnCreateAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller_ = new Controller();
                if (controller_.isUserLoggedIn(getApplicationContext())) {
                    intent = new Intent(MainList.this, CreateAdvertActivity.class);
                    startActivity(intent);
                } else {
                    UserInfo.SelectedPage = KeyCodes.MainToCreateAdvert;
                    intent = new Intent(MainList.this, LogInActivity.class);
                    startActivity(intent);
                }
            }
        });

        btnMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller_ = new Controller();
                if (controller_.isUserLoggedIn(getApplicationContext())) {
                    UserInfo.SelectedPage = KeyCodes.MainToMyPage;
                    UserInfo.MethodName = "GetUserAdvertList";
                    intent = new Intent(MainList.this, AdvertListActivity.class);
                    startActivity(intent);
                } else {
                    UserInfo.SelectedPage = KeyCodes.MainToMyPage;
                    intent = new Intent(MainList.this, LogInActivity.class);
                    startActivity(intent);
                }
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller_ = new Controller();
                if (controller_.isUserLoggedIn(getApplicationContext())) {
                    intent = new Intent(MainList.this, SettingsActivity.class);
                    startActivity(intent);
                } else {
                    UserInfo.SelectedPage = KeyCodes.MainToSettings;
                    intent = new Intent(MainList.this, LogInActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
