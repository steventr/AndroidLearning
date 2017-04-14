package itp341.truong.steven.a8;

import java.util.ArrayList;

/**
 * Created by Steven on 3/30/2016.
 */
public class NoteSingleton {


    private static NoteSingleton instance;
    private ArrayList<Note> notes;

    private NoteSingleton() {
        notes = new ArrayList<Note>();

        Note firstDefaultNote = new Note();
        firstDefaultNote.setTitle("First Default Note!");

        Note secondDefaultNote = new Note();
        secondDefaultNote.setTitle("Hey, A Second Default Note!");

        notes.add(firstDefaultNote);
        notes.add(secondDefaultNote);
    }

    public static NoteSingleton get() {
        if (instance == null) {
            instance = new NoteSingleton();
        }
        return instance;
    }

    public void addNote(Note n) {
        notes.add(n);
    }

    public void removeNote(int i) {
        if (notes.size() > 0) {
            if (i >= 0 && i < notes.size()) {
                notes.remove(i);
            }
        }
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

}
