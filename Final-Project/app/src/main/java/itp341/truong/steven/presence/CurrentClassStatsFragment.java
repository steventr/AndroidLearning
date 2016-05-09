package itp341.truong.steven.presence;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.Tools;
import com.db.chart.model.Bar;
import com.db.chart.model.BarSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.HorizontalBarChartView;
import com.db.chart.view.XController;
import com.db.chart.view.YController;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.ElasticEase;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CurrentClassStatsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CurrentClassStatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentClassStatsFragment extends Fragment {

    private HorizontalBarChartView chart;
    private OnClassStatsFragmentInteractionListener mListener;

    private String currentClassID;
    private int Divider;
    private ArrayList<String> studentIDArrayList;
    private BarSet data;

    private int[] colors;

    public CurrentClassStatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CurrentClassStatsFragment.
     */

    public static CurrentClassStatsFragment newInstance(String currentClassID) {
        CurrentClassStatsFragment fragment = new CurrentClassStatsFragment();
        Bundle args = new Bundle();
        args.putString("classID",currentClassID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentClassID = getArguments().getString("classID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_current_class_stats, container, false);
        colors = getContext().getResources().getIntArray(R.array.androidcolors);
        studentIDArrayList = new ArrayList<String>();
        chart = (HorizontalBarChartView) v.findViewById(R.id.attendanceGraph);

        chart.setAxisBorderValues(0, 100, 10)
                .setXAxis(false)
                .setYAxis(false)
                .setXLabels(XController.LabelPosition.OUTSIDE)
                .setYLabels(YController.LabelPosition.OUTSIDE)
                .setLabelsColor(Color.parseColor("#ffffff"))
                .setAxisColor(Color.parseColor("#ffffff"));

        chart.setAxisLabelsSpacing(Tools.fromDpToPx(40));

        data = new BarSet();

        chart.addData(data);

        updateChart();

        return v;
    }

    private void updateChart() {
        final Firebase classRef = new Firebase(FirebaseConstants.CLASSES + currentClassID );

        classRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("sessions").exists()) {
                    Divider = Integer.valueOf(dataSnapshot.child("sessions").getValue().toString());
                }
                else {
                    Divider = 1;
                }
                
                classRef.child("students").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        studentIDArrayList.add(dataSnapshot.getValue().toString());

                        Firebase student = new Firebase(FirebaseConstants.MEMBERS + dataSnapshot.getValue());
                        student.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                float toBeDivided = 0.0f;
                                if (dataSnapshot.child("attendance").child(currentClassID).exists()) {
                                    toBeDivided = (float) Integer.valueOf(dataSnapshot.child("attendance").child(currentClassID).getValue().toString());
                                }
                                float value =  (toBeDivided / Divider) * 100.0f;
                                Bar tobeAdded = new Bar(dataSnapshot.child("name").getValue().toString(), value);

                                Random randomInt = new Random();
                                tobeAdded.setColor(colors[randomInt.nextInt(colors.length)]);
                                data.addBar(tobeAdded);

                                if(chart.getData().size() == 1) {
                                    chart.show();
                                }
                                else
                                {
                                    chart.notifyDataUpdate();
                                }

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
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
        if (context instanceof OnClassStatsFragmentInteractionListener) {
            mListener = (OnClassStatsFragmentInteractionListener) context;
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
    public interface OnClassStatsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
