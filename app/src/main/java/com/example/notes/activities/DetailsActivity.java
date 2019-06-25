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
import com.example.notes.presenters.BasePresenter;
import com.example.notes.presenters.DetailsActivityPresenter;

public class DetailsActivity extends AppCompatActivity implements BasePresenter.View {

    private DetailsActivityPresenter presenter;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        presenter = new DetailsActivityPresenter(this);
        editText = findViewById(R.id.editTextNote);

        editText.setText(presenter.loadNoteText());

    }

    @Override
    protected void onPause() {
        presenter.onPause();
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
                presenter.deleteNote();
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

    public String getNoteText(){
        return editText.getText().toString();
    }

    public void setNoteText(String text){
        editText.setText(text);
    }

    public int getNoteID(){
        Intent intent = getIntent();
        return intent.getIntExtra(MainActivity.EXTRA_NOTE_ID, -1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
