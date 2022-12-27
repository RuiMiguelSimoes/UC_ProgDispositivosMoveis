package com.example.happens;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class eventPage extends AppCompatActivity {

    public TextView eventTitle;
    public TextView local;
    public TextView infoEvnt;
    public ImageView imageView;
    public EditText actCodeBox;
    public Button button;
    public Button delete;


    List<String> eventStuff = new ArrayList<String>();
    DBCon DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_event_page);

        DB = new DBCon(this);

        eventTitle = findViewById(R.id.eventTitle);
        local = findViewById(R.id.local);
        infoEvnt = findViewById(R.id.infoEvnt);
        imageView = findViewById(R.id.imageView);
        actCodeBox = findViewById(R.id.actCodeInput);
        button = findViewById(R.id.submitAct);
        delete = findViewById(R.id.delete);
        Button btnVoltar = findViewById(R.id.voltar_actEventPage);

        //butão para voltar a trás TIAGO seu idiota!! n havia botão! AAAAAAA a unica forma de voltar era apagar o evento xD
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(eventPage.this, MainScreen.class));
            }
        });

        getEvent();



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitCode();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });
    }

    @SuppressLint("Range")
    public void getEvent(){
        eventStuff.clear();
        Cursor information = DB.getEvents();

        SharedPreferences userInf = getSharedPreferences("userInf", MODE_PRIVATE);

        information.moveToFirst();
        for(int i = 0; i < information.getCount(); i++){
            if(information.getInt(information.getColumnIndex("idEnvt")) == userInf.getInt("eventId", -1)){
                System.out.println(DatabaseUtils.dumpCurrentRowToString(information));

                eventTitle.setText(information.getString(information.getColumnIndex("name")));
                infoEvnt.setText(information.getString(information.getColumnIndex("info")));

                if(information.getString(information.getColumnIndex("local")) != null)
                    local.setText(information.getString(information.getColumnIndex("local")));

                if(information.getInt(information.getColumnIndex("isActive")) == 1 && information.getInt(information.getColumnIndex("idUser")) == DB.getUserId(userInf.getString("username", null))){
                    actCodeBox.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                }
                else {
                    actCodeBox.setVisibility(View.INVISIBLE);
                    button.setVisibility(View.INVISIBLE);
                }

                if(information.getInt(information.getColumnIndex("idUser")) == DB.getUserId(userInf.getString("username", null)) || userInf.getString("username", null).equals("admin")){
                    delete.setVisibility(View.VISIBLE);
                }
                else {
                    delete.setVisibility(View.INVISIBLE);
                }
            }

            information.moveToNext();
        }
    }

    @SuppressLint("Range")
    public void submitCode(){
        Cursor information = DB.getEvents();
        SharedPreferences userInf = getSharedPreferences("userInf", MODE_PRIVATE);

        information.moveToFirst();
        for(int i = 0; i < information.getCount(); i++){
            if(information.getInt(information.getColumnIndex("idEnvt")) == userInf.getInt("eventId", -1)){
                if(Integer.parseInt(actCodeBox.getText().toString()) == information.getInt(information.getColumnIndex("actCode"))){
                    DB.uptadeUptadeAct(2, userInf.getInt("eventId", -1));

                    Intent reload = new Intent(eventPage.this, eventPage.class);
                    startActivity(reload);
                }
            }


            information.moveToNext();
        }
    }

    @SuppressLint("Range")
    public void delete(){
        Cursor information = DB.getEvents();
        SharedPreferences userInf = getSharedPreferences("userInf", MODE_PRIVATE);

        information.moveToFirst();
        for(int i = 0; i < information.getCount(); i++){
            if(information.getInt(information.getColumnIndex("idEnvt")) == userInf.getInt("eventId", -1)){
                DB.uptadeUptadeAct(3, userInf.getInt("eventId", -1));

                startActivity(new Intent(eventPage.this, MainScreen.class));
            }

            information.moveToNext();
        }
    }


}