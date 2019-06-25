package com.example.notes.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.notes.dao.NoteDAO;
import com.example.notes.entities.Note;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDAO getNoteDAO();
}
