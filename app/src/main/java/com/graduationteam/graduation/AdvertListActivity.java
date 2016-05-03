package com.graduationteam.graduation;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import adapters.AdvertAdapter;
import adapters.SpinnerAdapter;
import entities.Advert;
import entities.UserInfo;
import entities.WebServiceMethod;

public class AdvertListActivity extends AppCompatActivity {

    public static int[] categoryIcons_ = {R.drawable.iconcategorypleaseselect,
            R.drawable.iconcategoryhouse,
            R.drawable.iconcategoryfurniture,
            R.drawable.iconcategoryhomemate,
            R.drawable.iconcategoryelectronic,
            R.drawable.iconcategorybook,
            R.drawable.iconcategoryothers};
    public static int[] subIcons_ = {R.drawable.iconcategorypleaseselect};
    ImageButton btnAdvertListOrder_;
    ListView listAdvert_;
    Spinner subCategory_;
    List<Advert> advertList;
    Advert advert;
    AdvertAdapter advertAdapter;
    SpinnerAdapter adapterSpinner;
    ProgressDialog progressDialog;
    GetUserAdvertList task;
    WebServiceMethod method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_list);

        btnAdvertListOrder_ = (ImageButton) findViewById(R.id.btnAdvertListOrder);
        listAdvert_ = (ListView) findViewById(R.id.listAdvert);
        subCategory_ = (Spinner) findViewById(R.id.spinnerAdvertList);

        adapterSpinner = new SpinnerAdapter(AdvertListActivity.this, getResources().getStringArray(R.array.MainCategories), categoryIcons_);
        subCategory_.setAdapter(adapterSpinner);
        task = new GetUserAdvertList();
        task.execute();
    }

    private class GetUserAdvertList extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                method = new WebServiceMethod("GetUserAdvertList", "Object");

                method.request.addProperty("UserID_", UserInfo.UserID);

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
                        advertList.add(advert);
                    }
                    advertAdapter = new AdvertAdapter(AdvertListActivity.this, R.layout.custom_advert_list, advertList);
                    listAdvert_.setAdapter(advertAdapter);
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
