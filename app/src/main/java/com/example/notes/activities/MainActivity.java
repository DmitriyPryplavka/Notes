package com.example.notes.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.notes.R;
import com.example.notes.presenters.BasePresenter;
import com.example.notes.presenters.MainActivityPresenter;
import com.example.notes.enums.SortOrder;
import com.example.notes.adapters.NoteAdapter;
import com.example.notes.entities.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements BasePresenter.View {
    private NoteAdapter mAdapter;
    private List<Note> myDataset = new ArrayList<>();
    private MainActivityPresenter presenter;

    public static String EXTRA_NOTE_ID = "com.example.notes.NoteID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //Sort from new to old
            case R.id.sortAsc: {
                presenter.sort(SortOrder.NewFirst);
                break;
            }
            //Sort from old to new
            case R.id.sortDes: {
                presenter.sort(SortOrder.OldFirst);
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void init(){

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Change option menu icon
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.ic_menu_sort_by_size);
        toolbar.setOverflowIcon(drawable);

        presenter = new MainActivityPresenter(this);

        //Connect searchView with adapter
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.searchSubmit(query, searchView);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.searchSubmit(newText, null);
                return true;
            }
        });

        //Create new note
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            startActivity(intent);
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        presenter.loadNotes(myDataset);

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

    public NoteAdapter getAdapter(){
        return mAdapter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
