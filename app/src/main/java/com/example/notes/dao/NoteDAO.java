package com.example.notes.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notes.entities.Note;

import java.util.List;

@Dao
public interface NoteDAO {

    @Insert
    void addNote(Note note);

    @Query("SELECT * FROM note WHERE id = :id")
    Note getNote(int id);

    @Query("SELECT * FROM note")
    List<Note> getAllNotes();

    @Update
    int updateNote(Note note);

    @Delete
    void deleteNote(Note note);

}
