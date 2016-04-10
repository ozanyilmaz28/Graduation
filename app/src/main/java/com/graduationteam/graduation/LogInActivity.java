package com.graduationteam.graduation;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import entities.UserInfo;
import entities.WebServiceMethod;

public class LogInActivity extends AppCompatActivity {

    private static ProgressDialog progressDialog;
    private static EditText txtUserNameOrEmail;
    private static EditText txtPassword;
    private static Button btnLogIn;

    private static String userNameOrEmail;
    private static String password;
    LoginMethod task;
    WebServiceMethod method;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        txtUserNameOrEmail = (EditText) findViewById(R.id.LoginUsername);
        txtPassword = (EditText) findViewById(R.id.LoginPassword);
        btnLogIn = (Button) findViewById(R.id.LogInButton);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNameOrEmail = txtUserNameOrEmail.getText().toString();
                password = txtPassword.getText().toString();

                task = new LoginMethod();
                task.execute();
            }
        });
    }

    private class LoginMethod extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                method = new WebServiceMethod("DoLoginAndReturnUserInfo", "Object");

                method.request.addProperty("UsernameOrMail_", userNameOrEmail);
                method.request.addProperty("Password_", password);

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
                    method.objResultData = (SoapObject) method.objResult.getProperty("Data");

                    UserInfo.UserID = Long.parseLong(method.objResultData.getProperty("USER_ID").toString());
                    UserInfo.UserName = method.objResultData.getProperty("USER_CODE").toString();
                    UserInfo.NameSurname = method.objResultData.getProperty("USER_NAMESURNAME").toString();
                    UserInfo.Email = method.objResultData.getProperty("USER_EMAIL").toString();
                    UserInfo.Phone = method.objResultData.getProperty("USER_PHONE").toString();

                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    editor = sharedPreferences.edit();
                    editor.putBoolean("IsUserLoggedIn", true);
                    editor.putLong("UserID", UserInfo.UserID);
                    editor.putString("UserName", UserInfo.UserName);
                    editor.putString("NameSurname", UserInfo.NameSurname);
                    editor.putString("Email", UserInfo.Email);
                    editor.putString("Phone", UserInfo.Phone);
                    editor.commit();
                } else {
                    Toast.makeText(LogInActivity.this, method.objResult.getProperty("Message").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LogInActivity.this);
            progressDialog.setMessage("İşlem Gerçekleştiriliyor. Lütfen Bekleyiniz...");
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
