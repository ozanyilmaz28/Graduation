package com.graduationteam.graduation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpActivity extends Activity {

    Button singUp = (Button) findViewById(R.id.btnSignUp);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
