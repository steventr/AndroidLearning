package itp341.truong.steven.presence;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link itp341.truong.steven.presence.SignInKioskFragment.OnKioskFragmentLInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignInKioskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInKioskFragment extends Fragment {

    private EditText pinOne, pinTwo, pinThree, pinFour;
    private OnKioskFragmentLInteractionListener mListener;

    String mClassID;
    public SignInKioskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment SignInKioskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignInKioskFragment newInstance(String classID) {
        SignInKioskFragment fragment = new SignInKioskFragment();
        Bundle args = new Bundle();
        args.putString("classID", classID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mClassID = getArguments().getString("classID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_in_kiosk, container, false);

        pinOne = (EditText) v.findViewById(R.id.firstPinNumber);
        pinOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == pinOne.length()) {
                    pinTwo.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pinTwo = (EditText) v.findViewById(R.id.secondPinNumber);
        pinTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == pinOne.length()) {
                    pinThree.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pinThree = (EditText) v.findViewById(R.id.thirdPinNumber);
        pinThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == pinOne.length()) {
                    pinFour.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pinFour = (EditText) v.findViewById(R.id.fourthPinNumber);
        pinFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Verify pin now
                if (pinOne.getText().toString().length() == 1 && pinTwo.getText().toString().length() == 1 && pinThree.getText().toString().length() == 1) {
                    String pin = pinOne.getText().toString() + pinTwo.getText().toString() + pinThree.getText().toString() + pinFour.getText().toString();
                    SignIn(mClassID, pin);
                    pinOne.setText("");
                    pinTwo.setText("");
                    pinThree.setText("");
                    pinFour.setText("");
                    pinOne.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        incrementSessions();

        return v;
    }

    //The hope is that each time you open the kiosk, it's a new day
    private void incrementSessions() {
        //Increment sign in count
        final Firebase classRef = new Firebase(FirebaseConstants.CLASSES + mClassID);

        classRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("sessions").exists()) {
                    classRef.child("sessions").setValue(Integer.valueOf(dataSnapshot.child("sessions").getValue().toString()) + 1);
                } else {
                    classRef.child("sessions").setValue(1);
                }
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
        if (context instanceof OnKioskFragmentLInteractionListener) {
            mListener = (OnKioskFragmentLInteractionListener) context;
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnKioskFragmentLInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void SignIn(final String classID, final String pin) {
        //Increment sign in count
        Firebase classRef = new Firebase(FirebaseConstants.CLASSES + classID + "/students/");

        classRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(pin).exists()) {
                    //Successful sign in
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    final Firebase studentRef = new Firebase(FirebaseConstants.MEMBERS + dataSnapshot.child(pin).getValue());
                    studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child("attendance").child(classID).exists()) {
                                studentRef.child("attendance").child(classID).setValue(Integer.valueOf(dataSnapshot.child("attendance").child(classID).getValue().toString()) + 1);
                            } else {
                                studentRef.child("attendance").child(classID).setValue(1);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
                else {
                    Toast.makeText(getContext(), "Pin doesn't exist in this class!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
