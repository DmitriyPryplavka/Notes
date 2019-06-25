package com.example.notes.presenters;

import android.util.Log;

import com.example.notes.activities.DetailsActivity;
import com.example.notes.dao.NoteDAO;
import com.example.notes.entities.Note;

import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DetailsActivityPresenter extends BasePresenter{

    private Note note;

    public DetailsActivityPresenter(View view) {
        super(view);
        loadNote();
    }

    @Override
    public void atachView(View view) {
        loadNote();
        super.atachView(view);
    }

    public String loadNoteText(){
        return (note != null) ? note.getText() : "";
    }

    private void loadNote() {
        NoteDAO db = (NoteDAO) getModel();
        DetailsActivity da = (DetailsActivity) getView();
        if (da.getNoteID() != -1) {
            getDisposable().add(db.getNote(da.getNoteID())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(n -> {note = n; da.setNoteText(n.getText()); },
                            throwable -> Log.e(DetailsActivity.class.getSimpleName(), "Unable to get note" , throwable ))
            );
        } else {
            note = null;
        }
    }

    public void deleteNote(){
        NoteDAO db = (NoteDAO) getModel();
        getDisposable().add(Observable.fromCallable(() -> db.deleteNote(note))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> Log.i(DetailsActivity.class.getSimpleName(), "Delete note in database"),
                        throwable -> Log.e(DetailsActivity.class.getSimpleName(), "Unable to delete note", throwable))
        );
    }

    private void addNote(){
        NoteDAO db = (NoteDAO) getModel();
        DetailsActivity da = (DetailsActivity) getView();
        note = new Note(da.getNoteText(), new Date());
        getDisposable().add(Observable.fromCallable(() -> db.addNote(note))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> Log.i(DetailsActivity.class.getSimpleName(), "Add new note to database"),
                        throwable -> Log.e(DetailsActivity.class.getSimpleName(), "Unable to add new note", throwable))
        );
    }

    private void updateNote(){
        NoteDAO db = (NoteDAO) getModel();
        DetailsActivity da = (DetailsActivity) getView();
        note.setText(da.getNoteText());
        note.setDateTime(new Date());
        getDisposable().add(Observable.fromCallable(() -> db.updateNote(note))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> Log.i(DetailsActivity.class.getSimpleName(), "Update note in database"),
                        throwable -> Log.e(DetailsActivity.class.getSimpleName(), "Unable to update note", throwable))
        );
    }

    public void onPause(){
        DetailsActivity da = (DetailsActivity) getView();
        if (!da.getNoteText().trim().isEmpty())
            if (note == null){
                addNote();
            } else if (!note.getText().equals(da.getNoteText())){
                updateNote();
            }

    }
}
