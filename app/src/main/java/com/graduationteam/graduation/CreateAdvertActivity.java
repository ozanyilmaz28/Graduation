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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import entities.UserInfo;
import entities.WebServiceMethod;

public class CreateAdvertActivity extends Activity {

    int CAMERA_PIC_REQUEST = 0;
    String pictureImagePath = "";

    ImageButton imgBtnTakePhoto;
    Button saveAdvert;
    String arrayMainCategory_[];
    Spinner spinnerMainCategory_;
    ArrayAdapter adapterSpinner;

    String image_;

    ProgressDialog progressDialog;
    SaveAdvert task;
    WebServiceMethod method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        arrayMainCategory_ = new String[CategoryListActivity.categoryTexts_.length + 1];
        arrayMainCategory_[0] = "Ana Kategori Seçiniz";
        for (int i = 1; i <= CategoryListActivity.categoryTexts_.length; i++)
            arrayMainCategory_[i] = CategoryListActivity.categoryTexts_[i - 1];

        spinnerMainCategory_ = (Spinner) findViewById(R.id.createAdvertMainCategory);
        adapterSpinner = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, arrayMainCategory_);
        spinnerMainCategory_.setAdapter(adapterSpinner);

        imgBtnTakePhoto = (ImageButton) findViewById(R.id.mainImage);
        saveAdvert = (Button) findViewById(R.id.pageCreateAdvertBtnSave);

        image_ = getBase64ImageString();

        saveAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new SaveAdvert();
                task.execute();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            File imgFile = new File(pictureImagePath);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imgBtnTakePhoto.setImageBitmap(myBitmap);
            }
        }
    }

    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

    private void openBackCamera() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, 1);
    }

    private String getBase64ImageString() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.iconaddphoto);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        return Base64.encodeToString(stream.toByteArray(), 0);
    }

    private class SaveAdvert extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                method = new WebServiceMethod("DoInsertAndUpdateAdvert", "Object");

                method.request.addProperty("AdvertID_", 0);
                method.request.addProperty("AdvertMainTypeID_", 1);
                method.request.addProperty("AdvertSubTypeID_", 0);
                method.request.addProperty("AdvertDescription_", "test cümlesi");
                method.request.addProperty("UserID_", UserInfo.UserID);
                method.request.addProperty("Phone_", "542 542 42 42");
                method.request.addProperty("Mail_", "test@gmail.com");
                method.request.addProperty("Image_", image_);

                method.Method();

            } catch (Exception e) {
                e.printStackTrace();
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
                } else {
                    Toast.makeText(CreateAdvertActivity.this, method.objResult.getProperty("Message").toString(), Toast.LENGTH_SHORT).show();
                }
            }
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
