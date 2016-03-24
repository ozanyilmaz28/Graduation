package com.graduationteam.graduation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import adapters.ImageAdapter;
import controller.Controller;

public class MainList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(this));

        Button btnCategory = (Button) findViewById(R.id.btnCategory);
        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iCategoryList_ = new Intent(MainList.this, CategoryListActivity.class);
                startActivity(iCategoryList_);

            }
        });

        Button btnCreateAdvert = (Button) findViewById(R.id.btnCreateAdvert);
        btnCreateAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controller controller_ = new Controller();
                if (controller_.isUserLoggedIn(getApplicationContext())) {
                    Intent iCreateAdvert_ = new Intent(MainList.this, CreateAdvertActivity.class);
                    startActivity(iCreateAdvert_);
                } else {
                    Intent iLogin_ = new Intent(MainList.this, LogInActivity.class);
                    startActivity(iLogin_);
                }
            }
        });

    }
}
