package com.graduationteam.graduation;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CreateAdvertActivity extends Activity {

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

    }
}
