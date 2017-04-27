package csci571.truong.steven.hw9;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;

import csci571.truong.steven.hw9.dummy.DummyContent.DummyItem;
import csci571.truong.steven.hw9.models.FBObjectInstance;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class AlbumsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private View mView;
    private OnListFragmentInteractionListener mListener;
    private FBObjectInstance mData;
    private ExpandableListView listView;
    private AlbumExpandableListAdapter mAdapter;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     * @param data
     */
    public void setData(FBObjectInstance data) {
        if (data != null) {
            mData = data;
            updateAlbumsFragment(mData);
        }
    }

    public static AlbumsFragment newInstance(FBObjectInstance data) {
        AlbumsFragment fragment = new AlbumsFragment();
        fragment.setData(data);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_album_layout, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        listView = (ExpandableListView) mView.findViewById(R.id.lvExp);
        if (listView.getAdapter() == null && mAdapter != null) {
            listView.setAdapter(mAdapter);
            listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                private int current = -1;
                @Override
                public void onGroupExpand(int groupPosition) {
                    if (groupPosition != current) {
                        listView.collapseGroup(current);
                    }
                    current = groupPosition;
                }
            });
        }
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

        if (mAdapter == null) {
            if (mData != null ) {
                mAdapter = new AlbumExpandableListAdapter(Arrays.asList(mData.getAlbums()), context);
                    if (listView != null) {
                        listView.setAdapter(mAdapter);
                        listView.setGroupIndicator(null);
                    }
                }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updateAlbumsFragment(FBObjectInstance data) {
        mData = data;

        if (mView != null) {
            mView.findViewById(R.id.noContent).setVisibility(View.VISIBLE);
        }
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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}
