package com.graduationteam.graduation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateAdvertActivity extends Activity {

    int CAMERA_PIC_REQUEST = 0;
    String pictureImagePath = "";

    ImageButton imgBtnTakePhoto;
    String arrayMainCategory_[];
    Spinner spinnerMainCategory_;
    ArrayAdapter adapterSpinner;

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

        imgBtnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBackCamera();
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
}
