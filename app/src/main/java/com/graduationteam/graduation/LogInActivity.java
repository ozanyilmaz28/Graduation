package com.graduationteam.graduation;

import android.app.ProgressDialog;
import android.content.Intent;
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

import entities.KeyCodes;
import entities.UserInfo;
import entities.WebServiceMethod;

public class LogInActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    EditText txtUserNameOrEmail, txtPassword;
    Button btnLogIn, btnSignUp;

    String userNameOrEmail, password;

    LoginMethod task;
    WebServiceMethod method;
    Intent newPage_;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        txtUserNameOrEmail = (EditText) findViewById(R.id.LoginUsername);
        txtPassword = (EditText) findViewById(R.id.LoginPassword);
        btnLogIn = (Button) findViewById(R.id.LogInButton);
        btnSignUp = (Button) findViewById(R.id.LogInSignUp);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPage_ = new Intent(LogInActivity.this, SignUpActivity.class);
                UserInfo.SelectedPage = KeyCodes.LogInToSingUp;
                startActivity(newPage_);
                finish();
            }
        });
    }

    public void checkFields() {
        userNameOrEmail = txtUserNameOrEmail.getText().toString().trim();
        password = txtPassword.getText().toString().trim();

        if (!userNameOrEmail.equals("")) {
            if (!password.equals("")) {
                task = new LoginMethod();
                task.execute();
            } else {
                Toast.makeText(LogInActivity.this, "Şifre Boş Bırakılamaz!", Toast.LENGTH_SHORT).show();
                txtPassword.requestFocus();
            }
        } else {
            Toast.makeText(LogInActivity.this, "Kullanıcı Adı Boş Bırakılamaz!", Toast.LENGTH_SHORT).show();
            txtUserNameOrEmail.requestFocus();
        }
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
                    UserInfo.Password = method.objResultData.getProperty("USER_PASSWORD").toString();

                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    editor = sharedPreferences.edit();
                    editor.putBoolean("IsUserLoggedIn", true);
                    editor.putLong("UserID", UserInfo.UserID);
                    editor.putString("UserName", UserInfo.UserName);
                    editor.putString("NameSurname", UserInfo.NameSurname);
                    editor.putString("Email", UserInfo.Email);
                    editor.putString("Phone", UserInfo.Phone);
                    editor.putString("Password", UserInfo.Password);
                    editor.commit();

                    if (UserInfo.SelectedPage == KeyCodes.MainToCreateAdvert) {
                        newPage_ = new Intent(LogInActivity.this, CreateAdvertActivity.class);
                        startActivity(newPage_);
                        finish();
                    } else if (UserInfo.SelectedPage == KeyCodes.MainToMyPage) {
                        UserInfo.MethodName = "GetUserAdvertList";
                        newPage_ = new Intent(LogInActivity.this, AdvertListActivity.class);
                        startActivity(newPage_);
                        finish();
                    } else if (UserInfo.SelectedPage == KeyCodes.MainToSettings) {
                        newPage_ = new Intent(LogInActivity.this, SettingsActivity.class);
                        startActivity(newPage_);
                        finish();
                    }
                } else {
                    Toast.makeText(LogInActivity.this, method.objResult.getProperty("Message").toString(), Toast.LENGTH_SHORT).show();
                    txtUserNameOrEmail.setText("");
                    txtPassword.setText("");
                    txtPassword.requestFocus();
                }
            } else
                Toast.makeText(LogInActivity.this, "Web Service'ten Cevap Alınamıyor!", Toast.LENGTH_SHORT).show();
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
