package com.example.notes.diffutils;

import androidx.recyclerview.widget.DiffUtil;

import com.example.notes.entities.Note;

import java.util.List;

public class NoteDiffUtilCallback extends DiffUtil.Callback {

    private final List<Note> oldList;
    private final List<Note> newList;

    public NoteDiffUtilCallback(List<Note> oldList, List<Note> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Note oldNote = oldList.get(oldItemPosition);
        Note newNote = newList.get(newItemPosition);
        return oldNote.getId() == newNote.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Note oldNote = oldList.get(oldItemPosition);
        Note newNote = newList.get(newItemPosition);
        return oldNote.getText().equals(newNote.getText())
                && oldNote.getDateTime().equals(newNote.getDateTime());
    }
}
