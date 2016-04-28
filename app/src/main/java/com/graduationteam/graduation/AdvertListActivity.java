package com.graduationteam.graduation;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.util.List;

import adapters.AdvertAdapter;
import entities.AdvertList;
import entities.UserInfo;
import entities.WebServiceMethod;

public class AdvertListActivity extends AppCompatActivity {

    Button btnAdvertListOrder_;
    ListView listAdvert_;
    Spinner subCategory_;

    List<AdvertList> advertListEntity;
    AdvertAdapter advertAdapter;

    ProgressDialog progressDialog;
    GetAdvertList task;
    WebServiceMethod method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_list);

        btnAdvertListOrder_ = (Button)findViewById(R.id.btnAdvertListOrder);
        listAdvert_ = (ListView)findViewById(R.id.listAdvert);
        subCategory_ = (Spinner)findViewById(R.id.spinnerAdvertList);

        task = new GetAdvertList();
        task.execute();
    }

    private class GetAdvertList extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                method = new WebServiceMethod("DoInsertAndUpdateAdvert", "Object");

                method.request.addProperty("AdvertMainCategoryID_", UserInfo.SelectedMainCategory);

                method.Method();

            } catch (Exception e) {
                Toast.makeText(AdvertListActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            if (method.intPropertyCount == 1) {
                method.objResult = (SoapObject) method.objMain.getProperty(0);
                if (Boolean.parseBoolean(method.objResult.getProperty("Success").toString())) {

                } else {
                    Toast.makeText(AdvertListActivity.this, method.objResult.getProperty("Message").toString(), Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(AdvertListActivity.this, "Web Servis ile Bağlantı Kurulamadı!", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(AdvertListActivity.this);
            progressDialog.setMessage("İşlem Gerçekleştiriliyor. Lütfen Bekleyiniz...");
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
