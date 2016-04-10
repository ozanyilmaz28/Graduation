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

    Button btnCategory, btnCreateAdvert;
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
                    intent = new Intent(MainList.this, LogInActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
