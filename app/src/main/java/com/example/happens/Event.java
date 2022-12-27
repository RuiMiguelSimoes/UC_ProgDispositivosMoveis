package com.example.happens;

import android.location.Location;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Event {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public String name;
    public String desc;
    public Location location;

    public Event(){

    }

    public  Event(String name, String desc, Location location){
        this.name=name;
        this.desc=desc;
        this.location= location;
    }

    public  Event(String name, String desc){
        this.name=name;
        this.desc=desc;
    }

    public void saveNewEvent(String name, String desc) {
        Event newEvent = new Event(this.name, this.desc);
        CollectionReference eventsCollection = firestore.collection("Events");
        eventsCollection.document(this.name).set(newEvent);
    }

    public void saveNewEventWithLocation(String name, String desc, Location location){
        Event newEvent = new Event(this.name, this.desc, this.location);
        CollectionReference eventsCollection = firestore.collection("Events");
        eventsCollection.document(this.name).set(newEvent);
    }
}
