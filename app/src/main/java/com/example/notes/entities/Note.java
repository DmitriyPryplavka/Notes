package com.example.notes.entities;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Entity
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String text;
    @Ignore
    private String date;
    @Ignore
    private String time;
    private Date dateTime;
    @Ignore
    public Note(){}

    public Note(String text, Date dateTime) {
        this.text = text;
        this.dateTime = dateTime;
        this.date = parseDate(dateTime);
        this.time = parseTime(dateTime);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
        this.date = parseDate(dateTime);
        this.time = parseTime(dateTime);
    }

    private String parseDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return dateFormat.format(date);
    }

    private String parseTime(Date date){
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return id == note.getId() &&
                text.equals(note.getText()) &&
                dateTime.equals(note.getDateTime());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id, text, dateTime);
    }
}
