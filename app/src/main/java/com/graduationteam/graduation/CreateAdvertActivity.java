package com.graduationteam.graduation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import adapters.SpinnerAdapter;
import entities.UserInfo;
import entities.WebServiceMethod;

public class CreateAdvertActivity extends Activity {

    public static int[] categoryIcons_ = {R.drawable.iconcategorypleaseselect,
            R.drawable.iconcategoryhouse,
            R.drawable.iconcategoryfurniture,
            R.drawable.iconcategoryhomemate,
            R.drawable.iconcategoryelectronic,
            R.drawable.iconcategorybook,
            R.drawable.iconcategoryothers};

    public static int[] subIcons_ = {R.drawable.iconcategorypleaseselect};

    ImageView imgBtnTakePhoto;
    Button saveAdvert, btnCreateAdvertTakePhoto;
    Spinner spinnerMainCategory_, spinnerSubCategory_;
    SpinnerAdapter adapterSpinner, subSpinner;
    String baseImage_, image_;
    ProgressDialog progressDialog;
    SaveAdvert task;
    WebServiceMethod method;
    EditText edtDescription_, edtPhone_, edtMail_, edtPrice_;
    int subID_;

    //Fields for sending
    int selectedMainCategoryID_, selectedSubCategoryID_;
    String advertDescription_, advertPhone_, advertMail_, advertPrice_, selectedSubCategoryDesc_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        edtDescription_ = (EditText) findViewById(R.id.pageCreateAdvertEdtDescription);
        edtPhone_ = (EditText) findViewById(R.id.pageCreateAdvertEdtPhone);
        edtMail_ = (EditText) findViewById(R.id.pageCreateAdvertEdtMail);
        edtPrice_ = (EditText) findViewById(R.id.pageCreateAdvertEdtPrice);
        imgBtnTakePhoto = (ImageView) findViewById(R.id.mainImage);
        spinnerMainCategory_ = (Spinner) findViewById(R.id.createAdvertMainCategory);
        spinnerSubCategory_ = (Spinner) findViewById(R.id.createAdvertSubCategory);
        saveAdvert = (Button) findViewById(R.id.pageCreateAdvertBtnSave);
        btnCreateAdvertTakePhoto = (Button) findViewById(R.id.btnCreateAdvertTakePhoto);

        spinnerSubCategory_.setVisibility(View.GONE);
        edtPhone_.setText(UserInfo.Phone);
        edtMail_.setText(UserInfo.Email);

        adapterSpinner = new SpinnerAdapter(CreateAdvertActivity.this, getResources().getStringArray(R.array.MainCategories), categoryIcons_);
        spinnerMainCategory_.setAdapter(adapterSpinner);

        spinnerMainCategory_.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (spinnerMainCategory_.getSelectedItemPosition() != 0 && spinnerMainCategory_.getSelectedItemPosition() != 1 && spinnerMainCategory_.getSelectedItemPosition() != 6) {
                    spinnerSubCategory_.setVisibility(View.VISIBLE);
                    selectedSubCategoryID_ = 0;
                    if (spinnerMainCategory_.getSelectedItemPosition() == 2)
                        subID_ = R.array.SubCategories2;
                    if (spinnerMainCategory_.getSelectedItemPosition() == 3)
                        subID_ = R.array.SubCategories3;
                    if (spinnerMainCategory_.getSelectedItemPosition() == 4)
                        subID_ = R.array.SubCategories4;
                    if (spinnerMainCategory_.getSelectedItemPosition() == 5)
                        subID_ = R.array.SubCategories5;
                    subSpinner = new SpinnerAdapter(CreateAdvertActivity.this, getResources().getStringArray(subID_), subIcons_);
                    spinnerSubCategory_.setAdapter(subSpinner);

                    spinnerSubCategory_.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedSubCategoryDesc_ = getResources().getStringArray(subID_)[position];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else {
                    spinnerSubCategory_.setVisibility(View.GONE);
                    selectedSubCategoryID_ = -1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        baseImage_ = getBase64ImageString();

        saveAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });

        btnCreateAdvertTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private String getBase64ImageString() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.iconaddphoto);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }

    private void checkFields() {
        selectedMainCategoryID_ = spinnerMainCategory_.getSelectedItemPosition();
        if (spinnerSubCategory_.getVisibility() == View.GONE)
            selectedSubCategoryID_ = -1;
        else
            selectedSubCategoryID_ = spinnerSubCategory_.getSelectedItemPosition();
        advertDescription_ = edtDescription_.getText().toString().trim();
        advertPhone_ = edtPhone_.getText().toString().trim();
        advertMail_ = edtMail_.getText().toString().trim();
        advertPrice_ = edtPrice_.getText().toString().trim();
        image_ = "";

        if (selectedMainCategoryID_ > 0) {
            if (selectedSubCategoryID_ != 0) {
                if (!advertDescription_.equals(null) && !advertDescription_.equals("")) {
                    if (advertDescription_.length() >= 30) {
                        if ((!advertPhone_.equals(null) && !advertPhone_.equals("")) || (!advertMail_.equals(null) && !advertMail_.equals(""))) {
                            if (!advertPrice_.equals(null) && !advertPrice_.equals("")) {
                                task = new SaveAdvert();
                                task.execute();
                            } else {
                                Toast.makeText(CreateAdvertActivity.this, "Fiyat boş bırakılamaz...", Toast.LENGTH_SHORT).show();
                                edtPrice_.requestFocus();
                            }
                        } else {
                            Toast.makeText(CreateAdvertActivity.this, "Telefon numarası ya da email adresi giriniz...", Toast.LENGTH_SHORT).show();
                            edtPhone_.requestFocus();
                        }
                    } else {
                        Toast.makeText(CreateAdvertActivity.this, "Açıklama en az 30 karakter olmalıdır...", Toast.LENGTH_SHORT).show();
                        edtDescription_.requestFocus();
                    }
                } else {
                    Toast.makeText(CreateAdvertActivity.this, "Lütfen açıklama giriniz...", Toast.LENGTH_SHORT).show();
                    edtDescription_.requestFocus();
                }
            } else {
                Toast.makeText(CreateAdvertActivity.this, "Lütfen alt kategori seçiniz...", Toast.LENGTH_SHORT).show();
                spinnerSubCategory_.requestFocus();
            }

        } else {
            Toast.makeText(CreateAdvertActivity.this, "Lütfen ana kategori seçiniz...", Toast.LENGTH_SHORT).show();
            spinnerMainCategory_.requestFocus();
        }
    }

    private class SaveAdvert extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                method = new WebServiceMethod("DoInsertAndUpdateAdvert", "Object");

                method.request.addProperty("AdvertID_", 0);
                method.request.addProperty("AdvertMainTypeID_", selectedMainCategoryID_);
                method.request.addProperty("AdvertSubTypeDescription_", selectedSubCategoryDesc_);
                method.request.addProperty("AdvertDescription_", advertDescription_);
                method.request.addProperty("UserID_", UserInfo.UserID);
                method.request.addProperty("Phone_", advertPhone_);
                method.request.addProperty("Mail_", advertMail_);
                method.request.addProperty("Image_", baseImage_);
                method.request.addProperty("Price_", Integer.parseInt(advertPrice_));
                method.request.addProperty("TR_", true);

                method.Method();

            } catch (Exception e) {
                Toast.makeText(CreateAdvertActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            if (method.intPropertyCount == 1) {
                method.objResult = (SoapObject) method.objMain.getProperty(0);
                if (Boolean.parseBoolean(method.objResult.getProperty("Success").toString())) {
                    Toast.makeText(CreateAdvertActivity.this, method.objResult.getProperty("Message").toString(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CreateAdvertActivity.this, method.objResult.getProperty("Message").toString(), Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(CreateAdvertActivity.this, "Web Servis ile Bağlantı Kurulamadı!", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(CreateAdvertActivity.this);
            progressDialog.setMessage("İşlem Gerçekleştiriliyor. Lütfen Bekleyiniz...");
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
