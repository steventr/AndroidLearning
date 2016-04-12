package itp341.truong.steven.a8;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

public class NoteEditActivity extends AppCompatActivity {

    EditText titleEdit, contentEdit;

    Button save;
    Button delete;

    int noteIndex;
    Note currentNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        titleEdit = (EditText) findViewById(R.id.titleEditText);
        contentEdit = (EditText) findViewById(R.id.contentEditText);

        save = (Button) findViewById(R.id.saveNoteButton);
        delete = (Button) findViewById(R.id.deleteNoteButton);

        Intent i = getIntent();
        noteIndex = i.getIntExtra("NOTE_INDEX", -1);

        //New Note!
        if (noteIndex == -1) {
            currentNote = new Note();
        }
        else {
            currentNote = NoteSingleton.get().getNotes().get(noteIndex);
            titleEdit.setText(currentNote.getTitle());
            contentEdit.setText(currentNote.getContent());
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentNote.setTitle(titleEdit.getText().toString());
                currentNote.setContent(contentEdit.getText().toString());
                currentNote.setDate(new Date());

                if (noteIndex == -1 ) {
                    NoteSingleton.get().getNotes().add(currentNote);
                }
                else {
                    NoteSingleton.get().getNotes().set(noteIndex, currentNote);
                }

                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noteIndex == -1 ) {
                    //Don't need to do anything
                }
                else {
                    NoteSingleton.get().getNotes().remove(noteIndex);
                }
                finish();
            }
        });
    }
}
