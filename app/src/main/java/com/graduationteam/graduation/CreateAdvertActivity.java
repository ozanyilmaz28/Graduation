package com.graduationteam.graduation;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.media.Image;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import org.ksoap2.serialization.SoapObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import adapters.SpinnerAdapter;
import entities.Advert;
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

    LinearLayout layout_;
    ImageView imgBtnTakePhoto;
    Button saveAdvert;
    ImageButton btnCreateAdvertTakePhoto;
    Spinner spinnerMainCategory_, spinnerSubCategory_;
    SpinnerAdapter adapterSpinner, subSpinner;
    String image_;
    ProgressDialog progressDialog;
    SaveAdvert task;
    WebServiceMethod method;
    EditText edtDescription_, edtPhone_, edtMail_, edtPrice_;
    int subID_;
    boolean isPriceMandatory_;

    //Fields for sending
    int selectedMainCategoryID_, selectedSubCategoryID_;
    String advertDescription_, advertPhone_, advertMail_, advertPrice_, selectedSubCategoryDesc_;

    //Foto Çekimi
    int TAKE_PHOTO_CODE = 0;
    String imagePath = "", baseImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        layout_ = (LinearLayout) findViewById(R.id.createAdvertLinearLayout);
        edtDescription_ = (EditText) findViewById(R.id.pageCreateAdvertEdtDescription);
        edtPhone_ = (EditText) findViewById(R.id.pageCreateAdvertEdtPhone);
        edtMail_ = (EditText) findViewById(R.id.pageCreateAdvertEdtMail);
        edtPrice_ = (EditText) findViewById(R.id.pageCreateAdvertEdtPrice);
        imgBtnTakePhoto = (ImageView) findViewById(R.id.mainImage);
        spinnerMainCategory_ = (Spinner) findViewById(R.id.createAdvertMainCategory);
        spinnerSubCategory_ = (Spinner) findViewById(R.id.createAdvertSubCategory);
        saveAdvert = (Button) findViewById(R.id.pageCreateAdvertBtnSave);
        btnCreateAdvertTakePhoto = (ImageButton) findViewById(R.id.btnCreateAdvertTakePhoto);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Advert advtDataFromList = (Advert) extras.getSerializable("AdvertWithDetail");
            if (advtDataFromList != null)
                doManageVisibility(advtDataFromList);
            else
                Toast.makeText(CreateAdvertActivity.this, "Intent kullanım hatası!", Toast.LENGTH_SHORT).show();

        } else {
            spinnerSubCategory_.setVisibility(View.GONE);
            edtPhone_.setText(UserInfo.Phone);
            edtMail_.setText(UserInfo.Email);

            adapterSpinner = new SpinnerAdapter(CreateAdvertActivity.this, getResources().getStringArray(R.array.MainCategories), categoryIcons_);
            spinnerMainCategory_.setAdapter(adapterSpinner);

            spinnerMainCategory_.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    isPriceMandatory_ = true;
                    if (spinnerMainCategory_.getSelectedItemPosition() != 0 && spinnerMainCategory_.getSelectedItemPosition() != 1 && spinnerMainCategory_.getSelectedItemPosition() != 6) {
                        spinnerSubCategory_.setVisibility(View.VISIBLE);
                        selectedSubCategoryID_ = 0;
                        if (spinnerMainCategory_.getSelectedItemPosition() == 2)
                            subID_ = R.array.SubCategories2;
                        if (spinnerMainCategory_.getSelectedItemPosition() == 3) {
                            isPriceMandatory_ = false;
                            subID_ = R.array.SubCategories3;
                        }
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
                    if (isPriceMandatory_)
                        edtPrice_.setVisibility(View.VISIBLE);
                    else
                        edtPrice_.setVisibility(View.GONE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

            saveAdvert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkFields();
                }
            });

            btnCreateAdvertTakePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performAddPhoto();
                }
            });

            imgBtnTakePhoto.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(intent);*/
                }
            });

            baseImage = getBase64ImageString();

        }
    }

    private void performAddPhoto() {
        String timeStamp = getCurrentTimeStamp();
        String fileName = "Food_Shot_" + timeStamp + ".jpg";

        this.imagePath = Environment.getExternalStorageDirectory() + "/images/" + fileName;

        File file = new File(this.imagePath);
        file.getParentFile().mkdirs();

        try {
            file.createNewFile();
        } catch (Exception e) {
            Log.d(e.toString(), "");
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        startActivityForResult(intent, TAKE_PHOTO_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PHOTO_CODE) {
            if (resultCode == RESULT_OK) {
                this.onPhotoTaken();
            }
        }
    }

    private void onPhotoTaken() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        Bitmap bitmap = BitmapFactory.decodeFile(this.imagePath, options);
        try {
            ExifInterface ei = new ExifInterface(this.imagePath.toString());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
            }
        } catch (Exception e) {
            Log.d(e.toString(), "");
        }

        this.imgBtnTakePhoto.setImageBitmap(bitmap);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        return retVal;
    }

    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    private String getBase64ImageString() {
        BitmapDrawable drawable = (BitmapDrawable) imgBtnTakePhoto.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, stream);
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
        image_ = getBase64ImageString();

        if (selectedMainCategoryID_ > 0) {
            if (selectedSubCategoryID_ != 0) {
                if (!advertDescription_.equals(null) && !advertDescription_.equals("")) {
                    if (advertDescription_.length() >= 30) {
                        if ((!advertPhone_.equals(null) && !advertPhone_.equals("")) || (!advertMail_.equals(null) && !advertMail_.equals(""))) {
                            if ((advertPrice_.equals(null) || advertPrice_.equals("")) && isPriceMandatory_) {
                                Toast.makeText(CreateAdvertActivity.this, "Fiyat boş bırakılamaz...", Toast.LENGTH_SHORT).show();
                                edtPrice_.requestFocus();
                            } else {
                                if (!isPriceMandatory_)
                                    advertPrice_ = "0";
                                if (Integer.parseInt(advertPrice_) <= 0 && isPriceMandatory_) {
                                    Toast.makeText(CreateAdvertActivity.this, "Fiyat 0'dan büyük olmalıdır...", Toast.LENGTH_SHORT).show();
                                    edtPrice_.requestFocus();
                                } else {
                                    task = new SaveAdvert();
                                    task.execute();
                                }
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
                if (!baseImage.equals(image_))
                    method.request.addProperty("Image_", image_);
                else
                    method.request.addProperty("Image_", "");
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

    public void doManageVisibility(Advert data_) {
        if (!data_.getAdvtImageLink().equals("-")) {
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .showImageOnLoading(R.drawable.iconmainlistnotfoundbigger)
                    .build();

            LinearLayout linearLayout_ = (LinearLayout)findViewById(R.id.createAdvertLinearLayout);
            linearLayout_.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            imgBtnTakePhoto.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            imageLoader.displayImage(data_.getAdvtImageLink(), imgBtnTakePhoto, options);
        }

        edtDescription_.setText(data_.getAdvtDescription());
        edtMail_.setText(data_.getAdvtMail());
        edtPhone_.setText(data_.getAdvtPhone());
        if (data_.advtPrice > 0)
            edtPrice_.setText(String.valueOf(data_.advtPrice));
        else
            edtPrice_.setVisibility(View.GONE);

        String mainCat, subCat;
        if (data_.advtCategoryCode.indexOf("-") > -1) {
            mainCat = data_.advtCategoryCode.split("-")[0].trim();
            subCat = data_.advtCategoryCode.split("-")[1].trim();
            subSpinner = new SpinnerAdapter(CreateAdvertActivity.this, new String[]{subCat}, subIcons_);
            spinnerSubCategory_.setAdapter(subSpinner);
        } else {
            mainCat = data_.advtCategoryCode;
            spinnerSubCategory_.setVisibility(View.GONE);
        }
        adapterSpinner = new SpinnerAdapter(CreateAdvertActivity.this, new String[]{mainCat}, categoryIcons_);
        spinnerMainCategory_.setAdapter(adapterSpinner);

        edtDescription_.setEnabled(false);
        edtDescription_.setTextColor(Color.BLACK);
        edtMail_.setEnabled(false);
        edtMail_.setTextColor(Color.BLACK);
        edtPhone_.setEnabled(false);
        edtPhone_.setTextColor(Color.BLACK);
        edtPrice_.setEnabled(false);
        edtPrice_.setTextColor(Color.BLACK);
        spinnerSubCategory_.setEnabled(false);
        spinnerMainCategory_.setEnabled(false);

        btnCreateAdvertTakePhoto.setVisibility(View.GONE);
        saveAdvert.setVisibility(View.GONE);
    }
}
