package com.example.notes.presenters;

import android.util.Log;
import android.widget.SearchView;

import androidx.recyclerview.widget.DiffUtil;

import com.example.notes.activities.MainActivity;
import com.example.notes.dao.NoteDAO;
import com.example.notes.diffutils.NoteDiffUtilCallback;
import com.example.notes.entities.Note;
import com.example.notes.enums.SortOrder;
import com.example.notes.singleton.App;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivityPresenter extends BasePresenter{

    public MainActivityPresenter(MainActivity view) {
        super(view);
    }

    public void loadNotes(List<Note> noteList){
        NoteDAO db = (NoteDAO) getModel();
        MainActivity ma = (MainActivity) getView();
        getDisposable().add(db.getAllNotes().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(notes -> {
                    //Sort list of notes, new notes first
                    Collections.sort(notes, (n1, n2) -> n2.getDateTime().compareTo(n1.getDateTime()));

                    ma.getAdapter().updateData(noteList, notes);
                    noteList.addAll(notes);

                }, throwable -> Log.e(MainActivity.class.getSimpleName(), "Unable to select notes list from the database", throwable))
        );
    }

    public void searchSubmit(String query, SearchView searchView){
        MainActivity ma = (MainActivity) getView();
        ma.getAdapter().filter(query);
        if (searchView != null ) searchView.clearFocus();
    }

    public void sort(SortOrder so){
        MainActivity ma = (MainActivity) getView();
        List<Note> oldDataset = new ArrayList<>(ma.getAdapter().getData());

        if (so == SortOrder.NewFirst) {
            Collections.sort(ma.getAdapter().getData(), (n1, n2) -> n2.getDateTime().compareTo(n1.getDateTime()));
        } else {
            Collections.sort(ma.getAdapter().getData(), (n1, n2) -> n1.getDateTime().compareTo(n2.getDateTime()));
        }

        ma.getAdapter().updateData(oldDataset, ma.getAdapter().getData());
    }
}
