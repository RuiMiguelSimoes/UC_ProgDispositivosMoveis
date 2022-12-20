package com.example.happens;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class mainScreen extends AppCompatActivity {

    List<String> listEvents = new ArrayList<String>();
    List<String> cleanList = new ArrayList<String>();
    DBCon DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        DB = new DBCon(this);

        getEvents();

        ImageButton addEvnt = findViewById(R.id.addEvent);
        ImageButton refresh = findViewById(R.id.refresh);
        ListView listView = findViewById(R.id.eventList);

        addEvnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mainScreen.this, addEvnt.class));
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("RELOADING");
                Intent reload = new Intent(mainScreen.this, mainScreen.class);
                startActivity(reload);
            }
        });

        for(int i = listEvents.size() - 1; -1 < i; i--){
            String current = listEvents.get(i);
            System.out.println(current);

            String finalAdd = current.split("::::")[0];

            if(current.split("::::")[2].equals("0")){
                finalAdd+=("\n(Unofficial Event)");
            } else if(current.split("::::")[2].equals("1")){
                finalAdd+=("\n(Event in Authorization Process)");
            } else {
                finalAdd+=("\n(Official Event)");
            }


            cleanList.add(finalAdd);
        }
        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cleanList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(listEvents.get(listEvents.size() - i - 1).split("::::")[3]);
                SharedPreferences userInf = getSharedPreferences("userInf", MODE_PRIVATE);
                SharedPreferences.Editor userInfEdit = userInf.edit();

                userInfEdit.putInt("eventId", Integer.parseInt(listEvents.get(listEvents.size() - i - 1).split("::::")[3]));
                userInfEdit.commit();

                startActivity(new Intent(mainScreen.this, eventPage.class));
            }
        });
    }

    @SuppressLint("Range")
    public void getEvents(){
        listEvents.clear();
        Cursor information = DB.getEvents();
        information.moveToFirst();
        for(int i = 0; i < information.getCount(); i++){
            System.out.println(DatabaseUtils.dumpCurrentRowToString(information));

            if(information.getInt(information.getColumnIndex("isActive")) != 3) {
                String newListItem = information.getString(information.getColumnIndex("name")) + "::::" + information.getString(information.getColumnIndex("info")) + "::::" + information.getInt(information.getColumnIndex("isActive")) + "::::" + information.getInt(information.getColumnIndex("idEnvt"));
                System.out.println(newListItem);
                listEvents.add(newListItem);
            }

            information.moveToNext();
        }
    }
}