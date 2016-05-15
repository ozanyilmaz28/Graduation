package com.graduationteam.graduation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import adapters.AdvertAdapter;
import adapters.ImageAdapter;
import controller.Controller;
import entities.Advert;
import entities.KeyCodes;
import entities.UserInfo;
import entities.WebServiceMethod;

public class MainList extends AppCompatActivity {

    ProgressDialog progressDialog;
    Top15Advert task;
    WebServiceMethod method;

    Button btnCategory, btnCreateAdvert, btnMyPage, btnSettings;
    GridView gridView;
    Intent intent;

    List<Advert> advertList;
    Advert advert;
    ImageAdapter adapter_;

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

    private class Top15Advert extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                method = new WebServiceMethod("GetTop15AdvertList", "Object");

                method.Method();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (method.intPropertyCount == 1) {
                method.objResult = (SoapObject) method.objMain.getProperty(0);
                if (Boolean.parseBoolean(method.objResult.getProperty("Success").toString())) {
                    advertList = new ArrayList<Advert>();
                    method.objResultData = (SoapObject) method.objResult.getProperty("Data");
                    for (int i = 0; i < method.objResultData.getPropertyCount(); i++) {
                        method.objResultDetailsData = (SoapObject) method.objResultData.getProperty(i);
                        advert = new Advert();
                        advert.setAdvtID(Integer.parseInt(method.objResultDetailsData.getProperty("ID").toString()));
                        advert.setAdvtDateTime(method.objResultDetailsData.getProperty("Datetime").toString());
                        advert.setAdvtDescription(method.objResultDetailsData.getProperty("Description").toString());
                        advert.setAdvtPhone(method.objResultDetailsData.getProperty("Phone").toString());
                        advert.setAdvtMail(method.objResultDetailsData.getProperty("Mail").toString());
                        advert.setAdvtCategoryCode(method.objResultDetailsData.getProperty("MainCategoryCode").toString());
                        advert.setAdvtPrice(Integer.parseInt(method.objResultDetailsData.getProperty("Price").toString()));
                        advert.setAdvtImageLink(method.objResultDetailsData.getProperty("ImageLink").toString());
                        advertList.add(advert);
                    }
                    adapter_ = new ImageAdapter(getApplicationContext(), advertList);
                    gridView.setAdapter(adapter_);

                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                        }
                    });
                    progressDialog.dismiss();

                } else {
                    gridView.setAdapter(new ImageAdapter(getApplicationContext()));
                    progressDialog.dismiss();
                    Toast.makeText(MainList.this, method.objResult.getProperty("Message").toString(), Toast.LENGTH_SHORT).show();
                }
            } else {
                progressDialog.dismiss();
                Toast.makeText(MainList.this, "Web Service'ten Cevap Alınamıyor!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainList.this);
            progressDialog.setMessage("İşlem Gerçekleştiriliyor. Lütfen Bekleyiniz...");
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        task = new Top15Advert();
        task.execute();

    }

}
