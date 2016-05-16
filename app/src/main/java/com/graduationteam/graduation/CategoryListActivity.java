package com.graduationteam.graduation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import adapters.CategoryAdapter;

public class CategoryListActivity extends Activity {

    public static int[] categoryIcons_ = {R.drawable.iconcategoryhouse,
            R.drawable.iconcategoryfurniture,
            R.drawable.iconcategoryhomemate,
            R.drawable.iconcategoryelectronic,
            R.drawable.iconcategorybook,
            R.drawable.iconcategoryothers};

    public static String[] categoryTexts_;

    ListView lv;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        context = this;
        int size_ = getResources().getStringArray(R.array.MainCategories).length;
        categoryTexts_ = new String[size_ - 1];
        for (int i = 1; i < size_; i++)
            categoryTexts_[i - 1] = getResources().getStringArray(R.array.MainCategories)[i];
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(new CategoryAdapter(this, categoryTexts_, categoryIcons_));

    }
}
