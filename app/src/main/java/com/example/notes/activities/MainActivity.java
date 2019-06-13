package com.example.notes.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.notes.R;
import com.example.notes.dao.NoteDAO;
import com.example.notes.diffutils.NoteDiffUtilCallback;
import com.example.notes.singleton.App;
import com.example.notes.database.AppDatabase;
import com.example.notes.enums.SortOrder;
import com.example.notes.adapters.NoteAdapter;
import com.example.notes.entities.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private NoteAdapter mAdapter;
    private List<Note> myDataset = new ArrayList<>();
    private NoteDAO noteDAO;
    private final CompositeDisposable disposable = new CompositeDisposable();


    public static String EXTRA_NOTE_ID = "com.example.notes.NoteID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Change option menu icon
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.ic_menu_sort_by_size);
        toolbar.setOverflowIcon(drawable);

        AppDatabase db = App.getInstance().getDatabase();
        noteDAO = db.noteDAO();
        initList();

        //Connect searchView with adapter
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.filter(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.filter(newText);
                return true;
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            startActivity(intent);
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.sortAsc: {
                mAdapter.sort(SortOrder.NewFirst);
                break;
            }
            case R.id.sortDes: {
                mAdapter.sort(SortOrder.OldFirst);
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void initList() {
        disposable.add(noteDAO.getAllNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(notes -> {
                    NoteDiffUtilCallback noteDiffUtilCallback = new NoteDiffUtilCallback(myDataset, notes);
                    DiffUtil.DiffResult noteDiffResult = DiffUtil.calculateDiff(noteDiffUtilCallback);

                    myDataset.addAll(notes);
                    //Sort list of notes, new notes first
                    Collections.sort(myDataset, (n1, n2) -> n2.getDateTime().compareTo(n1.getDateTime()));

                    noteDiffResult.dispatchUpdatesTo(mAdapter);
                }, throwable -> Log.e(MainActivity.class.getSimpleName(), "Unable to select notes list from the database", throwable))
        );

        RecyclerView recyclerView = findViewById(R.id.myRecyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new NoteAdapter(myDataset, viewHolder -> {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            intent.putExtra(EXTRA_NOTE_ID, viewHolder.noteID);
            startActivity(intent);
        });
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        disposable.clear();
    }
}
