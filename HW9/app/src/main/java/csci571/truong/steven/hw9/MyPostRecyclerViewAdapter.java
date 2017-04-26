package csci571.truong.steven.hw9;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import csci571.truong.steven.hw9.PostsFragment.OnListFragmentInteractionListener;
import csci571.truong.steven.hw9.dummy.DummyContent.DummyItem;
import csci571.truong.steven.hw9.models.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPostRecyclerViewAdapter extends RecyclerView.Adapter<MyPostRecyclerViewAdapter.ViewHolder> {

    private final List<Post> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final String mUsername, mProfileURL;
    private final Context mContext;

    public MyPostRecyclerViewAdapter(List<Post> items, OnListFragmentInteractionListener listener, Context context, String username, String profileURL) {
        mValues = items;
        mListener = listener;
        mUsername = username;
        mProfileURL = profileURL;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mPostUser.setText(mUsername);
        holder.mPostDate.setText(mValues.get(position).getCreated_time());
        holder.mPostMessage.setText(mValues.get(position).getMessage());
        holder.setProfilePicture(mProfileURL);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void update(List<Post> datas){
        mValues.clear();
        mValues.addAll(datas);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView mProfilePicture;
        public final TextView mPostUser;
        public final TextView mPostDate;
        public final TextView mPostMessage;

        public Post mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPostUser = (TextView) view.findViewById(R.id.postUser);
            mPostDate = (TextView) view.findViewById(R.id.postDate);
            mPostMessage = (TextView) view.findViewById(R.id.postText);
            mProfilePicture = (ImageView) view.findViewById(R.id.profilePicture);
        }

        public void setProfilePicture(String url) {
            Picasso.with(mContext).load(url).into(mProfilePicture);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPostMessage.getText() + "'";
        }
    }
}
