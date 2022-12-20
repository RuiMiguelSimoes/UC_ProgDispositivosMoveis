package com.example.happens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText username, password, passwordCheck;
        Button reg, sign;
        DBCon DB;

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        passwordCheck = findViewById(R.id.checkPassword);

        reg = findViewById(R.id.submitRegister);
        sign = findViewById(R.id.signButton);

        SharedPreferences userInf = getSharedPreferences("userInf", MODE_PRIVATE);
        SharedPreferences.Editor userInfEdit = userInf.edit();

        DB = new DBCon(this);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strUsername = username.getText().toString();
                String strPassword = password.getText().toString();
                String strPasswordCheck = passwordCheck.getText().toString();

                if(strUsername.equals("")) {
                    username.setHintTextColor(Color.parseColor("#cc0000"));
                    username.setHint("Must enter an Username");
                }
                else if(strPassword.equals("")) {
                    password.setHintTextColor(Color.parseColor("#cc0000"));
                    password.setHint("Must enter an Password");
                }
                else if(strPasswordCheck.equals("")) {
                    passwordCheck.setHintTextColor(Color.parseColor("#cc0000"));
                    passwordCheck.setHint("Must confirm Password");
                }

                else if(!strPassword.equals("") && !strPasswordCheck.equals(strPassword))
                    passwordCheck.setTextColor(Color.parseColor("#cc0000"));

                else{
                    Boolean check4User = DB.doesUserExist(strUsername);
                    if(!check4User) {
                        Boolean insert = DB.insertData(strUsername, strPassword);
                        userInfEdit.putString("username", username.getText().toString());
                        userInfEdit.commit();
                        Toast.makeText(MainActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, mainScreen.class));
                    }
                    else
                        Toast.makeText(MainActivity.this, "Username Taken", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, login.class));
            }
        });

        passwordCheck.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                passwordCheck.setTextColor(Color.parseColor("#000000"));
            }
        });
    }
}