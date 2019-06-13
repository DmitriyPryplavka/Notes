package com.example.notes.singleton;

import android.app.Application;

import androidx.room.Room;

import com.example.notes.database.AppDatabase;

public class App extends Application {

    public static App instance;
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "NoteManager").build();
    }

    public static App getInstance(){
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }

}
