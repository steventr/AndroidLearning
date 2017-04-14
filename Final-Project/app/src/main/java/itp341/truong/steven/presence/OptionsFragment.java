package itp341.truong.steven.presence;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OptionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OptionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OptionsFragment extends Fragment {

    private String userID;
    private String oldEmail;

    private EditText email,password;

    private Button save;

    private OnOptionsFragmentInteractionListener mListener;

    public OptionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OptionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OptionsFragment newInstance(String userID) {
        OptionsFragment fragment = new OptionsFragment();
        Bundle args = new Bundle();
        args.putString("userID", userID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getString("userID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_options, container, false);

        email = (EditText) v.findViewById(R.id.currentUserEmail);
        password = (EditText) v.findViewById(R.id.currentUserPassword);

        save = (Button) v.findViewById(R.id.saveOptions);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (email.getText().length() > 0 && password.getText().length() > 0) {
                    Firebase ref = new Firebase("https://presence-manager.firebaseio.com/");
                    ref.changeEmail(oldEmail, password.getText().toString(), email.getText().toString(), new Firebase.ResultHandler() {
                        @Override
                        public void onSuccess() {
                            Firebase ref = new Firebase(FirebaseConstants.USERS + userID);
                            ref.child("email").setValue(email.getText().toString());
                            oldEmail = email.getText().toString();
                            Toast.makeText(getContext(), "Success.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            Toast.makeText(getContext(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

        });

        loadAccount();
        return v;
    }

    private void loadAccount() {
        Firebase accountRef = new Firebase(FirebaseConstants.USERS + userID);

        accountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                email.setText(dataSnapshot.child("email").getValue().toString());
                oldEmail = email.getText().toString();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
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
        if (context instanceof OnOptionsFragmentInteractionListener) {
            mListener = (OnOptionsFragmentInteractionListener) context;
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
    public interface OnOptionsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
