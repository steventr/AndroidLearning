package itp341.truong.steven.presence;

import android.content.Context;
import android.net.Uri;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StudentsInfoFragment.OnStudentsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudentsInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentsInfoFragment extends Fragment {

    private TextView summaryText;

    private String mCurrentUserID;
    private ArrayList<Member> mStudentsArray;

    MemberRecyclerViewAdapter adapter;

    private OnStudentsFragmentInteractionListener mListener;

    private Firebase firebaseMembersRef;

    public StudentsInfoFragment() {
        // Required empty public constructor
    }


    public static StudentsInfoFragment newInstance(String currentUserID) {
        StudentsInfoFragment fragment = new StudentsInfoFragment();

        Bundle args = new Bundle();
        args.putString("userID", currentUserID);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCurrentUserID = getArguments().getString("userID");
            mStudentsArray = new ArrayList<Member>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_students_info, container, false);

        summaryText = (TextView) v.findViewById(R.id.studentsSummary);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.student_list);
        if (recyclerView instanceof RecyclerView) {
            Context context = recyclerView.getContext();
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter = new MemberRecyclerViewAdapter(mStudentsArray, mCurrentUserID);
            recyclerView.setAdapter(adapter);
        }

        LoadStudents();
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStudentsFragmentInteractionListener) {
            mListener = (OnStudentsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void LoadStudents() {
        Firebase.setAndroidContext(getContext());
        firebaseMembersRef = new Firebase(FirebaseConstants.MEMBERS);
        firebaseMembersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addStudentFromID(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeStudentFromID(dataSnapshot.getKey());
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

    private void removeStudentFromID(String id) {
        Member toRemove = null;
        for(Member c : mStudentsArray) {
            if (c.memberID.equals(id)) {
                toRemove = c;
            }
        }

        if (toRemove != null) {
            mStudentsArray.remove(toRemove);
        }

        adapter.notifyDataSetChanged();
        summaryText.setText(summaryText());
    }

    public void addStudentFromID(final String id) {
        Firebase firebaseMemberRef = new Firebase(FirebaseConstants.MEMBERS + id);
        firebaseMemberRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Member m = new Member();
                m.memberID = id;
                m.name = dataSnapshot.child("name").getValue().toString();
                m.pin = dataSnapshot.child("pin").getValue().toString();

                mStudentsArray.add(m);
                adapter.notifyDataSetChanged();
                summaryText.setText(summaryText());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void updateStudentFromID(String id) {
        Member memberToUpdate = null;
        for(Member c : mStudentsArray) {
            if (c.memberID.equals(id)) {
                memberToUpdate = c;
                Log.d("Found", "CLASS FOUND IN mCLASS");
            }
        }

        Firebase firebaseMemberRef = new Firebase(FirebaseConstants.MEMBERS + id);
        final Member finalClassToUpdate = memberToUpdate;

        firebaseMemberRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                finalClassToUpdate.name = dataSnapshot.child("name").getValue().toString();
                finalClassToUpdate.pin = dataSnapshot.child("pin").getValue().toString();
                adapter.notifyDataSetChanged();
                summaryText.setText(summaryText());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
    public String summaryText() {
        return "You manage the " + (mStudentsArray.size() - 1) + " students below!";
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStudentsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
