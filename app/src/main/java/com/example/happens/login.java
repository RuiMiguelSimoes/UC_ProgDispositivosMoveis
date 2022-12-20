package com.example.happens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button log = findViewById(R.id.logIn);
        Button goReg = findViewById(R.id.goReg);

        EditText username = findViewById(R.id.nomeLog);
        EditText password = findViewById(R.id.passLog);

        SharedPreferences userInf = getSharedPreferences("userInf", MODE_PRIVATE);
        SharedPreferences.Editor userInfEdit = userInf.edit();

        DBCon DB = new DBCon(this);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DB.isPasswordCorrect(username.getText().toString(), password.getText().toString())){
                    userInfEdit.putString("username", username.getText().toString());
                    userInfEdit.commit();
                    startActivity(new Intent(login.this, mainScreen.class));
                }
            }
        });

        goReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this, MainActivity.class));
            }
        });
    }
}