package com.graduationteam.graduation;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import adapters.AdvertAdapter;
import adapters.SpinnerAdapter;
import entities.Advert;
import entities.KeyCodes;
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
    List<Advert> advertList, allList, selectedList;
    Advert advert;
    AdvertAdapter advertAdapter;
    SpinnerAdapter adapterSpinner;
    ProgressDialog progressDialog;
    GetUserAdvertList task;
    UpdateAdvertStatus taskUpdateStatus;
    WebServiceMethod method;
    int order_ = 0;

    String selectedSub = "";
    String[] cat;
    long SelectedAdvertID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_list);

        btnAdvertListOrder_ = (ImageButton) findViewById(R.id.btnAdvertListOrder);
        listAdvert_ = (ListView) findViewById(R.id.listAdvert);
        subCategory_ = (Spinner) findViewById(R.id.spinnerAdvertList);

        if (UserInfo.MethodName.equals("GetUserAdvertList"))
            adapterSpinner = new SpinnerAdapter(AdvertListActivity.this, getResources().getStringArray(R.array.MainCategories), categoryIcons_);
        else {
            if (UserInfo.SelectedMainCategory == 2)
                cat = getResources().getStringArray(R.array.SubCategories2);
            else if (UserInfo.SelectedMainCategory == 3)
                cat = getResources().getStringArray(R.array.SubCategories3);
            else if (UserInfo.SelectedMainCategory == 4)
                cat = getResources().getStringArray(R.array.SubCategories4);
            else if (UserInfo.SelectedMainCategory == 5)
                cat = getResources().getStringArray(R.array.SubCategories5);
            else {
                cat = new String[1];
                if (UserInfo.SelectedMainCategory == 1)
                    cat[0] = getResources().getStringArray(R.array.MainCategories)[1];
                else
                    cat[0] = getResources().getStringArray(R.array.MainCategories)[6];
                subCategory_.setEnabled(false);
            }
            adapterSpinner = new SpinnerAdapter(AdvertListActivity.this, cat, categoryIcons_);
        }
        subCategory_.setAdapter(adapterSpinner);
        task = new GetUserAdvertList();
        task.execute();
    }

    private class GetUserAdvertList extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                method = new WebServiceMethod(UserInfo.MethodName, "Object");
                if (UserInfo.MethodName.equals("GetUserAdvertList"))
                    method.request.addProperty("UserID_", UserInfo.UserID);
                else {
                    method.request.addProperty("AdvertMainTypeID_", UserInfo.SelectedMainCategory);
                }

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
                    allList = new ArrayList<Advert>();
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
                        advert.setAdvtIsOpen(Boolean.parseBoolean(method.objResultDetailsData.getProperty("IsOpen").toString()));
                        advertList.add(advert);
                    }
                    allList = advertList;
                    advertAdapter = new AdvertAdapter(AdvertListActivity.this, R.layout.custom_advert_list, advertList);
                    listAdvert_.setAdapter(advertAdapter);

                    btnAdvertListOrder_.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (order_ == 0) {
                                Collections.sort(advertList, new Comparator<Advert>() {
                                    @Override
                                    public int compare(Advert p1, Advert p2) {
                                        order_ = 1;
                                        return p1.getAdvtPrice() - p2.getAdvtPrice();
                                    }

                                });
                            } else
                                Collections.reverse(advertList);

                            advertAdapter = new AdvertAdapter(AdvertListActivity.this, R.layout.custom_advert_list, advertList);
                            listAdvert_.setAdapter(advertAdapter);
                        }
                    });

                    subCategory_.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            if (subCategory_.getSelectedItemPosition() == 0) {
                                advertList = allList;
                                advertAdapter = new AdvertAdapter(AdvertListActivity.this, R.layout.custom_advert_list, advertList);
                                listAdvert_.setAdapter(advertAdapter);
                            } else {
                                selectedList = new ArrayList<Advert>();
                                if (UserInfo.MethodName.equals("GetUserAdvertList"))
                                    selectedSub = getResources().getStringArray(R.array.MainCategories)[arg2];
                                else
                                    selectedSub = cat[arg2];
                                for (Advert item_ : advertList) {
                                    if (item_.getAdvtCategoryCode().indexOf(selectedSub) > -1)
                                        selectedList.add(item_);
                                }
                                advertAdapter = new AdvertAdapter(AdvertListActivity.this, R.layout.custom_advert_list, selectedList);
                                listAdvert_.setAdapter(advertAdapter);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                        }
                    });

                    if (UserInfo.SelectedPage == KeyCodes.MainToMyPage) {
                        listAdvert_.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                builder.setTitle(getResources().getString(R.string.CloseAdvert));
                                builder.setMessage(getResources().getString(R.string.AreYouSureToCloseAdvert))
                                        .setCancelable(false)
                                        .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                TextView txtListeIcerik = (TextView) view.findViewById(R.id.txtAdvertListID);
                                                SelectedAdvertID = Long.parseLong(txtListeIcerik.getText().toString());
                                                taskUpdateStatus = new UpdateAdvertStatus();
                                                taskUpdateStatus.execute();
                                            }
                                        })
                                        .setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                                return false;
                            }
                        });
                    }
                } else {
                    Toast.makeText(AdvertListActivity.this, method.objResult.getProperty("Message").toString(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(AdvertListActivity.this, "Web Servis ile Bağlantı Kurulamadı!", Toast.LENGTH_SHORT).show();
                finish();
            }
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

    private class UpdateAdvertStatus extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                method = new WebServiceMethod("DoUpdateAdvertStatus", "Object");

                method.request.addProperty("AdvertID_", SelectedAdvertID);
                method.request.addProperty("IsOpen", false);

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
                    Toast.makeText(AdvertListActivity.this, method.objResult.getProperty("Message").toString(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(AdvertListActivity.this, method.objResult.getProperty("Message").toString(), Toast.LENGTH_SHORT).show();
                }
                task = new GetUserAdvertList();
                task.execute();
            } else {
                Toast.makeText(AdvertListActivity.this, "Web Servis ile Bağlantı Kurulamadı!", Toast.LENGTH_SHORT).show();
            }
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
