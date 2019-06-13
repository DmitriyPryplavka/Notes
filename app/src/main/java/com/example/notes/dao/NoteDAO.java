package com.example.notes.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notes.entities.Note;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

@Dao
public interface NoteDAO {

    @Insert
    long addNote(Note note);

    @Query("SELECT * FROM note WHERE id = :id")
    Maybe<Note> getNote(int id);

    @Query("SELECT * FROM note")
    Flowable<List<Note>> getAllNotes();

    @Update
    int updateNote(Note note);

    @Delete
    int deleteNote(Note note);

}
