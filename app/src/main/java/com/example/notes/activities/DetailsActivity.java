package com.example.notes.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.notes.R;
import com.example.notes.dao.NoteDAO;
import com.example.notes.singleton.App;
import com.example.notes.database.AppDatabase;
import com.example.notes.entities.Note;

import java.util.Date;

public class DetailsActivity extends AppCompatActivity {
    private Note note;
    private AppDatabase db;
    private NoteDAO noteDAO;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        db = App.getInstance().getDatabase();
        noteDAO = db.noteDAO();
        Intent intent = getIntent();
        editText = findViewById(R.id.editTextNote);

        if (intent.getIntExtra(MainActivity.EXTRA_NOTE_ID, -1) != -1){
            note = noteDAO.getNote(intent.getIntExtra(MainActivity.EXTRA_NOTE_ID, -1));
            editText.setText(note.getText());
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
                noteDAO.addNote(note);
            } else if (!note.getText().equals(noteStr)){
                note.setText(noteStr);
                note.setDateTime(new Date());
                noteDAO.updateNote(note);
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
                noteDAO.deleteNote(note);
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
}
