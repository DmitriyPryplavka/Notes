package com.example.notes.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.notes.R;
import com.example.notes.dao.NoteDAO;
import com.example.notes.singleton.App;
import com.example.notes.database.AppDatabase;
import com.example.notes.entities.Note;

import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DetailsActivity extends AppCompatActivity {
    private Note note;
    private NoteDAO noteDAO;
    private EditText editText;
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        AppDatabase db = App.getInstance().getDatabase();
        noteDAO = db.noteDAO();
        Intent intent = getIntent();
        editText = findViewById(R.id.editTextNote);

        if (intent.getIntExtra(MainActivity.EXTRA_NOTE_ID, -1) != -1){
            disposable.add(noteDAO.getNote(intent.getIntExtra(MainActivity.EXTRA_NOTE_ID, -1))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(n -> {note = n; editText.setText(n.getText());},
                                throwable -> Log.e(DetailsActivity.class.getSimpleName(), "Unable to get note" , throwable ))
            );
        } else {
            editText.setText("");
        }
    }

    @Override
    protected void onPause() {
        String noteStr = editText.getText().toString();

        if (!noteStr.trim().isEmpty())
            if (note == null){
                note = new Note(noteStr, new Date());
                disposable.add(Observable.fromCallable(() -> noteDAO.addNote(note))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(success -> Log.e(DetailsActivity.class.getSimpleName(), "Add new note to database"),
                                throwable -> Log.e(DetailsActivity.class.getSimpleName(), "Unable to add new note", throwable))
                );
            } else if (!note.getText().equals(noteStr)){
                note.setText(noteStr);
                note.setDateTime(new Date());
                disposable.add(Observable.fromCallable(() -> noteDAO.updateNote(note))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(success -> Log.e(DetailsActivity.class.getSimpleName(), "Update note in database"),
                                throwable -> Log.e(DetailsActivity.class.getSimpleName(), "Unable to update note", throwable))
                );
            }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.deleteButton:{
                disposable.add(Observable.fromCallable(() -> noteDAO.deleteNote(note))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(success -> Log.e(DetailsActivity.class.getSimpleName(), "Delete note in database"),
                                throwable -> Log.e(DetailsActivity.class.getSimpleName(), "Unable to delete note", throwable))
                );
                onNavigateUp();
                break;
            }
            case R.id.shareButton:{
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = editText.getText().toString();
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT,"");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();

        disposable.clear();
    }
}
