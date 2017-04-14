package itp341.truong.steven.a8;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Steven on 3/30/2016.
 */
public class NotesAdapter  extends ArrayAdapter<Note> {

    public NotesAdapter(Context context, ArrayList<Note> notes) {
        super(context, 0, notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Note note = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_note, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.noteTitle);
        TextView date = (TextView) convertView.findViewById(R.id.noteDate);

        title.setText(note.getTitle());

        DateFormat formatter = new SimpleDateFormat("dd/MMM");
        date.setText(formatter.format(note.getDate()).toString());

        return convertView;
    }
}
