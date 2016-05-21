package com.graduationteam.graduation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.util.Locale;

import controller.Controller;
import entities.KeyCodes;
import entities.UserInfo;
import entities.WebServiceMethod;

public class SignUpActivity extends Activity {

    ProgressDialog progressDialog;
    Button singUp;
    EditText edtUsername, edtNameSurname, edtMail, edtPhone, edtPass, edtPassConfirm;
    String username_, nameSurname_, eMail_, phone_, pass_, passConfirm_;
    Controller cont_;
    Boolean isNewUser;

    SignUpMethod task;
    WebServiceMethod method;
    Intent newPage_;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        cont_ = new Controller();

        singUp = (Button) findViewById(R.id.btnSignUp);
        edtUsername = (EditText) findViewById(R.id.SignUpUserName);
        edtNameSurname = (EditText) findViewById(R.id.SignUpNameSurname);
        edtMail = (EditText) findViewById(R.id.SignUpEmail);
        edtPhone = (EditText) findViewById(R.id.SignUpPhoneNumber);
        edtPass = (EditText) findViewById(R.id.SignUpPassword);
        edtPassConfirm = (EditText) findViewById(R.id.SignUpPasswordConfirm);

        if (UserInfo.SelectedPage == KeyCodes.SettingsToSingUp) {
            isNewUser = false;
            edtUsername.setText(UserInfo.UserName);
            edtNameSurname.setText(UserInfo.NameSurname);
            edtMail.setText(UserInfo.Email);
            edtPhone.setText(UserInfo.Phone);
            edtPass.setText(UserInfo.Password);
            edtPassConfirm.setText(UserInfo.Password);
            edtUsername.setEnabled(false);
            singUp.setText(getResources().getString(R.string.btnSettingsUpdate));
        } else
            isNewUser = true;


        edtPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });
    }

    private void checkFields() {
        username_ = edtUsername.getText().toString().trim();
        nameSurname_ = edtNameSurname.getText().toString().trim();
        eMail_ = edtMail.getText().toString().trim();
        phone_ = edtPhone.getText().toString().trim();
        pass_ = edtPass.getText().toString().trim();
        passConfirm_ = edtPassConfirm.getText().toString().trim();

        if (!username_.equals((""))) {
            if (!nameSurname_.equals((""))) {
                if (!eMail_.equals((""))) {
                    if (cont_.isValidEmailAddress(eMail_)) {
                        if (!phone_.equals((""))) {
                            if (!pass_.equals((""))) {
                                if (!passConfirm_.equals((""))) {
                                    if (pass_.equals(passConfirm_)) {
                                        task = new SignUpMethod();
                                        task.execute();
                                    } else {
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.PasswordMatchingError), Toast.LENGTH_SHORT).show();
                                        edtPass.setText("");
                                        edtPassConfirm.setText("");
                                        edtPass.requestFocus();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.PasswordConfirmationCannotBeNull), Toast.LENGTH_SHORT).show();
                                    edtPassConfirm.requestFocus();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.PasswordCannotBeNull), Toast.LENGTH_SHORT).show();
                                edtPass.requestFocus();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.PhoneNumberCannotBeNull), Toast.LENGTH_SHORT).show();
                            edtPhone.requestFocus();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.InvalidMail), Toast.LENGTH_SHORT).show();
                        edtMail.setText("");
                        edtMail.requestFocus();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.MailCannotBeNull), Toast.LENGTH_SHORT).show();
                    edtMail.requestFocus();
                }
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.NameSurnameCannotBeNull), Toast.LENGTH_SHORT).show();
                edtUsername.requestFocus();
            }
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.UserNameCannotBeNull), Toast.LENGTH_SHORT).show();
            edtUsername.requestFocus();
        }
    }

    private class SignUpMethod extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                method = new WebServiceMethod("DoInsertAndUpdateUser", "Object");

                method.request.addProperty("IsNewUser_", isNewUser);
                method.request.addProperty("Username_", username_);
                method.request.addProperty("NameSurname_", nameSurname_);
                method.request.addProperty("Email_", eMail_);
                method.request.addProperty("Phone_", phone_);
                method.request.addProperty("Password_", pass_);
                if (getResources().getConfiguration().locale == new Locale("tr"))
                    method.request.addProperty("TR", true);
                else
                    method.request.addProperty("TR", false);

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

                    if (UserInfo.SelectedPage == KeyCodes.MainToCreateAdvert) {
                        newPage_ = new Intent(SignUpActivity.this, CreateAdvertActivity.class);
                        startActivity(newPage_);
                        finish();
                    } else
                        finish();
                    Toast.makeText(SignUpActivity.this, method.objResult.getProperty("Message").toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, method.objResult.getProperty("Message").toString(), Toast.LENGTH_SHORT).show();
                    edtPass.setText("");
                    edtPassConfirm.setText("");
                    edtUsername.setText("");
                    edtUsername.requestFocus();
                }
            } else
                Toast.makeText(SignUpActivity.this, getResources().getString(R.string.WebServiceConnectionError), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SignUpActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.Processing));
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
