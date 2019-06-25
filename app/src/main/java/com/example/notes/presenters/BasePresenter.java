package com.example.notes.presenters;

import com.example.notes.dao.NoteDAO;
import com.example.notes.singleton.App;

import io.reactivex.disposables.CompositeDisposable;

public class BasePresenter {

    public interface View{}
    public interface Model{}

    private Model model;
    private View view;

    private final CompositeDisposable disposable;

    public BasePresenter() {
        this.model = App.getInstance().getDatabase().getNoteDAO();
        this.disposable = new CompositeDisposable();
    }

    BasePresenter(View view) {
        this.view = view;
        this.model = App.getInstance().getDatabase().getNoteDAO();
        this.disposable = new CompositeDisposable();
    }

    public BasePresenter(NoteDAO model, View view) {
        this.model = model;
        this.view = view;
        this.disposable = new CompositeDisposable();
    }

    public void atachView(View view){
        this.view = view;
    }

    public void detachView(){
        this.view = null;
        this.disposable.clear();
    }

    Model getModel() {
        return model;
    }

    View getView() {
        return view;
    }

    CompositeDisposable getDisposable() {
        return disposable;
    }


}
