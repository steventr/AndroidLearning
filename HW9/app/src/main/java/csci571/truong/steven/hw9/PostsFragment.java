package csci571.truong.steven.hw9;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import csci571.truong.steven.hw9.dummy.DummyContent;
import csci571.truong.steven.hw9.dummy.DummyContent.DummyItem;
import csci571.truong.steven.hw9.models.FBObjectInstance;
import csci571.truong.steven.hw9.models.Post;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PostsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private FBObjectInstance mData;
    private String mProfileName, mProfilePictureURL;
    private MyPostRecyclerViewAdapter mAdapter;
    private View mView;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PostsFragment() {
        mData = new FBObjectInstance();
    }

    public void setData(FBObjectInstance data, String profileName, String profilePictureURL) {
        if (data != null) {
            mData = data;
            if (mAdapter != null) {
                updateContent(Arrays.asList(mData.getPosts()));
            }
            mProfileName = profileName;
            mProfilePictureURL = profilePictureURL;
        }
    }



    public static PostsFragment newInstance(FBObjectInstance data, String profileName, String profilePictureURL) {
        PostsFragment fragment = new PostsFragment();
        fragment.setData(data, profileName, profilePictureURL);
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
        mView = inflater.inflate(R.layout.fragment_post_list, container, false);

        View mViewRecyclerView = mView.findViewById(R.id.list);

        // Set the adapter
        if (mViewRecyclerView instanceof RecyclerView) {
            Context context = mViewRecyclerView.getContext();
            RecyclerView recyclerView = (RecyclerView) mViewRecyclerView;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mAdapter = new MyPostRecyclerViewAdapter(Arrays.asList(mData.getPosts()), mListener, getContext(), mProfileName, mProfilePictureURL );
            recyclerView.setAdapter(mAdapter);

            if (mData.getPosts().length == 0) {
                mView.findViewById(R.id.noContent).setVisibility(View.VISIBLE);
            }
        }

        return mView;
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
        void onListFragmentInteraction(Post item);
    }

    public void updateContent(List<Post> newContent) {
        mAdapter.update(newContent);
    }
}
