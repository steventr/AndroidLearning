package csci571.truong.steven.hw9;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import csci571.truong.steven.hw9.SearchFragmentPage.OnListFragmentInteractionListener;
import csci571.truong.steven.hw9.models.SearchResultObject;

import java.util.List;


public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {
    public static String PROFILE_NAME = "PROFILE_NAME";
    public static String PROFILE_PICTURE = "PROFILE_PICTURE";
    private Context mContext;
    private final List<SearchResultObject> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter(List<SearchResultObject> items, Context context, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_searchresult, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).getName());
        Picasso.with(mContext).load(mValues.get(position).getPicture().getData().getSrc()).into((ImageView) holder.mView.findViewById(R.id.profilePicture));
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

        ImageView favoritesButton = (ImageView) holder.mView.findViewById(R.id.favoriteIcon);
        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = (ImageView) v;
                if (imageView.getDrawable().getConstantState() == v.getResources().getDrawable(R.drawable.favorites_on).getConstantState())
                {
                    imageView.setImageDrawable( v.getResources().getDrawable(R.drawable.favorites_off));
                }
                else {
                    imageView.setImageDrawable( v.getResources().getDrawable(R.drawable.favorites_on));
                }
            }
        });

        ImageView detailsButton = (ImageView) holder.mView.findViewById(R.id.detailsChevron);
        detailsButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra(PROFILE_NAME, mValues.get(position).getName());
                intent.putExtra(PROFILE_PICTURE, mValues.get(position).getPicture().getData().getSrc());
                intent.putExtra(SearchResultsActivity.DETAILS_ID, mValues.get(position).getId());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public SearchResultObject mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
