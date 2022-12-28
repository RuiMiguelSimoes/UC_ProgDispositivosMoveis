package com.example.happens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_register);

        TextView btnLogin = findViewById(R.id.TextViewEntrar);
        Button btnRegister = findViewById(R.id.btnRegister_actRegister);

        EditText username = findViewById(R.id.name_register_activity);
        EditText password = findViewById(R.id.password_register_activity);
        EditText email = findViewById(R.id.email_register_activity);
        EditText passwordCheck = findViewById(R.id.confirmPassword_register_activity);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });

        String strUsername = username.getText().toString();
        String strPassword = password.getText().toString();
        String strEmail = email.getText().toString();
        String strPasswordCheck = passwordCheck.getText().toString();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(strUsername.equals("")) {
                    username.setHintTextColor(Color.parseColor("#cc0000"));
                    username.setHint("Must enter an Username");
                }

                if(strEmail.equals("")) {
                    email.setHintTextColor(Color.parseColor("#cc0000"));
                    email.setHint("Must enter an Username");
                }

                if(strPassword.equals("")) {
                    password.setHintTextColor(Color.parseColor("#cc0000"));
                    password.setHint("Must enter an Password");
                }
                if(strPasswordCheck.equals("")) {
                    passwordCheck.setHintTextColor(Color.parseColor("#cc0000"));
                    passwordCheck.setHint("Must confirm Password");
                }

                if(strPassword.equals("") && !strPasswordCheck.equals(strPassword))
                    passwordCheck.setTextColor(Color.parseColor("#cc0000"));

                else if(strPassword.equals(strPasswordCheck) ){

                        User newUser = new User();
                        newUser.email=email.getText().toString();
                        newUser.username=username.getText().toString();
                        newUser.password=password.getText().toString();

                        newUser.writeNewUser(newUser.username, newUser.email, newUser.password);

                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }

            }
        });
    }
}