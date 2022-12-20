package com.example.happens;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

public class DBCon extends SQLiteOpenHelper{

    public static final String DBName = "Login.db";

    public DBCon(@Nullable Context context) {
        super(context, "Login.db", null, 1);
    }

    private ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imageInBytes;

    @Override
    public void onCreate(SQLiteDatabase HappeningDB) {
        HappeningDB.execSQL("CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, username TEXT UNIQUE, password TEXT, age INTEGER, bio TEXT, course TEXT, year INTEGER, currentEvents INTEGER)");
        HappeningDB.execSQL("CREATE TABLE events(idEnvt INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, idUser INTEGER, name TEXT, info TEXT, isActive INTEGER, actCode TEXT, image BLOB DEFAULT NULL, local TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase HappeningDB, int i, int i1) {
        HappeningDB.execSQL("DROP TABLE IF EXISTS users");
        HappeningDB.execSQL("DROP TABLE IF EXISTS events");
    }

    public boolean insertData(String username, String password) {
        SQLiteDatabase HappeningDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("username", username);
        contentValues.put("password", password);

        long result = HappeningDB.insert("users", null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }


    public Boolean doesUserExist(String username){
        SQLiteDatabase HappeningDB = this.getWritableDatabase();
        Cursor cursor = HappeningDB.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});

        System.out.println(cursor.getCount());

        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean isPasswordCorrect(String username, String password){
        SQLiteDatabase HappeningDB = this.getWritableDatabase();
        Cursor cursor = HappeningDB.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[]{username, password});

        System.out.println(cursor.getCount());
        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public int getUserId(String username){
        SQLiteDatabase HappeningDB = this.getWritableDatabase();
        Cursor cursor = HappeningDB.rawQuery("SELECT id FROM users WHERE username = ?", new String[]{username});

        cursor.moveToFirst();

        System.out.println(cursor.getInt(0));
        return cursor.getInt(0);
    }

    public boolean createEvent(int userId, String name, String info, String actCode, @NonNull ModelClass objectModelClass, String local, int isActive) {
        SQLiteDatabase HappeningDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Bitmap imageToStoreBitmap = objectModelClass.getImage();
        objectByteArrayOutputStream = new ByteArrayOutputStream();
        imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG,100,objectByteArrayOutputStream);
        imageInBytes = objectByteArrayOutputStream.toByteArray();

        contentValues.put("idUser", userId);
        contentValues.put("name", name);
        contentValues.put("info", info);
        contentValues.put("actCode", actCode);
        contentValues.put("image", imageInBytes);
        contentValues.put("local", local);
        contentValues.put("isActive", isActive);

        long result = HappeningDB.insert("events", null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getEvents(){
        SQLiteDatabase HappeningDB = this.getWritableDatabase();
        Cursor cursor = HappeningDB.rawQuery("SELECT idEnvt, idUser, name, info, isActive, actCode, local FROM events", null);

        return cursor;
    }

    public void uptadeUptadeAct(int newAct, int eventId){
        SQLiteDatabase HappeningDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("isActive",newAct);

        HappeningDB.update("events", cv, "idEnvt = ?", new String[]{String.valueOf(eventId)});
    }
}
