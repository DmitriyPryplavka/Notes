package com.example.notes.holders;

import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.adaptors.NoteAdapter;
import com.example.notes.entities.Note;

public class NoteHolder extends RecyclerView.ViewHolder {
    public TextView textView;
    public TextView dateTextView;
    public TextView timeTextView;
    public int noteID;

    public NoteHolder(ConstraintLayout v) {
        super(v);
        textView = (TextView) v.getViewById(R.id.textViewNote);
        dateTextView = (TextView) v.getViewById(R.id.textViewDate);
        timeTextView = (TextView) v.getViewById(R.id.textViewTime);
    }

    public void bind(final NoteHolder holder, final NoteAdapter.OnItemClickListener listner, Note note) {
        noteID = note.getId();
        String noteText = (note.getText().length() > 100) ? note.getText().substring(0, 100) + "..." : note.getText();
        textView.setText(noteText);
        dateTextView.setText(note.getDate());
        timeTextView.setText(note.getTime());
        itemView.setOnClickListener(v -> listner.onItemClick(holder));
    }
}
