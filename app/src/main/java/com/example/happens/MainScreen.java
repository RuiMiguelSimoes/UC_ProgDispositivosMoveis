package com.example.happens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainScreen extends AppCompatActivity {

    List<String> listEvents = new ArrayList<String>();
    List<String> cleanList = new ArrayList<String>();
    DBCon DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_main_screen);

        DB = new DBCon(this);

        getEvents();

        ImageButton addEvnt = findViewById(R.id.addEvent);
        ImageButton refresh = findViewById(R.id.refresh);
        ListView listViewEvents = findViewById(R.id.eventList);

        addEvnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainScreen.this, addEvnt.class));
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("RELOADING");
                Intent reload = new Intent(MainScreen.this, MainScreen.class);
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

        listViewEvents.setAdapter(adapter);

        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(listEvents.get(listEvents.size() - i - 1).split("::::")[3]);
                SharedPreferences userInf = getSharedPreferences("userInf", MODE_PRIVATE);
                SharedPreferences.Editor userInfEdit = userInf.edit();

                userInfEdit.putInt("eventId", Integer.parseInt(listEvents.get(listEvents.size() - i - 1).split("::::")[3]));
                userInfEdit.commit();

                startActivity(new Intent(MainScreen.this, eventPage.class));
            }
        });
    }

    @SuppressLint("Range")
    public void getEvents(){
        listEvents.clear();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("Events").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            /*
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Event> lEventsList = new ArrayList<>();
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        Event event = document.toObject(Event.class);
                        lEventsList.add(event);
                    }
                    ListView EventsListView = (ListView) findViewById(R.id.eventList);
                    //Falta fazer o adapter calss
                    Adapter eventsAdapter = new Adapter(this,lEventsList ) {
                    };
                    EventsListView.setAdapter(lEventsList);
                    listEvents.add(newListItem);
                } else {
                    Log.d("MissionActivity", "Error getting documents: ", task.getException());
                }
            }
            */
        });

    }
}