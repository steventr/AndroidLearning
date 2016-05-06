package itp341.truong.steven.presence;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ClassesInfoFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private String mCurrentUserID;
    private List<Class> mClasses;
    private ClassRecyclerViewAdapter adapter;
    private OnListFragmentInteractionListener mListener;

    private Firebase firebaseUserRef;

    private TextView summaryText;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ClassesInfoFragment() {
    }


    public static ClassesInfoFragment newInstance(String currentUserID) {
        ClassesInfoFragment fragment = new ClassesInfoFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, 1);
        args.putString("userID", currentUserID);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mCurrentUserID = getArguments().getString("userID");
            mClasses = new ArrayList<Class>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_list, container, false);
        summaryText = (TextView) view.findViewById(R.id.classesSummary);
        // Set the adapter
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.classes_list);
        if (recyclerView instanceof RecyclerView) {
            Context context = recyclerView.getContext();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new ClassRecyclerViewAdapter(mClasses, mListener);
            recyclerView.setAdapter(adapter);
        }

        loadClasses(mCurrentUserID);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Class item);
    }

    void loadClasses(String currentID) {
        Firebase.setAndroidContext(getContext());
        firebaseUserRef = new Firebase(FirebaseConstants.USERS + "/" + currentID);
        firebaseUserRef.child("classes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addClassFromID(dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeClassFromID(dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        summaryText.setText(summaryText());
    }

    public void updateClassFromID(String id) {
        Class classToUpdate = null;
        for(Class c : mClasses) {
            if (c.id.equals(id)) {
                classToUpdate = c;
                Log.d("Found", "CLASS FOUND IN mCLASS");
            }
        }

        Firebase firebaseClassRef = new Firebase(FirebaseConstants.CLASSES + "/" + id);
        final Class finalClassToUpdate = classToUpdate;

        firebaseClassRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("UPDATECLASSFROMID", "UPDATING");
                finalClassToUpdate.name = dataSnapshot.child("name").getValue().toString();
                finalClassToUpdate.details = dataSnapshot.child("detail").getValue().toString();
                adapter.notifyDataSetChanged();
                summaryText.setText(summaryText());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void removeClassFromID(String id) {
        Class toRemove = null;
        for(Class c : mClasses) {
            if (c.id.equals(id)) {
               toRemove = c;
            }
        }

        if (toRemove != null) {
            mClasses.remove(toRemove);
        }

        adapter.notifyDataSetChanged();
        summaryText.setText(summaryText());
    }

    public void addClassFromID(final String id) {
        Firebase firebaseClassRef = new Firebase(FirebaseConstants.CLASSES + "/" + id);
        firebaseClassRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Class c = new Class();
                c.id = id;
                c.name = dataSnapshot.child("name").getValue().toString();
                c.details = dataSnapshot.child("detail").getValue().toString();
                mClasses.add(c);
                adapter.notifyDataSetChanged();
                summaryText.setText(summaryText());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public String summaryText() {
        return "You manage the " + (mClasses.size() - 1) + " classes below!";
    }
}
