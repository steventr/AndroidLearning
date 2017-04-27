package csci571.truong.steven.hw9;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import csci571.truong.steven.hw9.SearchFragmentPage.OnListFragmentInteractionListener;
import csci571.truong.steven.hw9.models.SearchResultObject;
import csci571.truong.steven.hw9.models.SearchType;

import java.util.ArrayList;
import java.util.List;


public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {
    public static String PROFILE_NAME = "PROFILE_NAME";
    public static String PROFILE_PICTURE = "PROFILE_PICTURE";
    private Context mContext;
    private final List<SearchResultObject> mValues;
    private final OnListFragmentInteractionListener mListener;
    private SearchPageAdapter mSearchPageAdapter;

    public MyItemRecyclerViewAdapter(List<SearchResultObject> items, Context context, OnListFragmentInteractionListener listener, SearchPageAdapter searchPageAdapter) {
        mValues = items;
        mListener = listener;
        mContext = context;
        mSearchPageAdapter = searchPageAdapter;
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
        if (Favorites.getInstance(mContext).exists(mContext, mValues.get(position).getId())) {
            favoritesButton.setImageDrawable( favoritesButton.getResources().getDrawable(R.drawable.favorites_on));
        }
        else {
            favoritesButton.setImageDrawable( favoritesButton.getResources().getDrawable(R.drawable.favorites_off));
        }

//        favoritesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ImageView imageView = (ImageView) v;
//
//                if (Favorites.getInstance(mContext).exists(mContext, mValues.get(position).getId())) {
//                    Favorites.getInstance(mContext).removeFavorite(mContext, mValues.get(position).getId());
//                    imageView.setImageDrawable( v.getResources().getDrawable(R.drawable.favorites_off));
//                }
//                else {
//                    Favorites.getInstance(mContext).addFavorite(mContext, mValues.get(position));
//                    imageView.setImageDrawable( v.getResources().getDrawable(R.drawable.favorites_on));
//                }
//
//                if (((SearchResultsActivity) mContext).isShowingOnlyFavorites) {
//                    ArrayList<ArrayList<SearchResultObject>> searchResultObjects = new ArrayList<ArrayList<SearchResultObject>>();
//                    for (int i = 0; i < 5; i++) {
//                        searchResultObjects.add(new ArrayList<SearchResultObject>());
//                    }
//
//                    for (SearchResultObject searchResultObject : Favorites.getInstance(mContext).getFavorites()) {
//                        searchResultObjects.get(SearchType.toInteger(searchResultObject.getType())).add(searchResultObject);
//                    }
//
//                    ArrayList<SearchResultObject[]> mData = new ArrayList<SearchResultObject[]>();
//                    for (int i = 0; i < searchResultObjects.size(); i++) {
//                        mData.add(searchResultObjects.get(i).toArray(new SearchResultObject[searchResultObjects.get(i).size()]));
//                    }
//
//                    for (int i = 0; i < 5; i++) {
//                        List<SearchResultObject> resultsList = new ArrayList<SearchResultObject>();
//                        for (int j = 0; j < mData.get(i).length; j++) {
//                            resultsList.add(mData.get(i)[j]);
//                        }
//
//                        if ( resultsList.size() > 0) {
//                            mSearchPageAdapter.updateResults(SearchType.fromInteger(i), resultsList.subList(0, Math.min(11, resultsList.size())));
//                        }
//                        else {
//                            mSearchPageAdapter.updateResults(SearchType.fromInteger(i), resultsList);
//                        }
//                    }
//                    notifyDataSetChanged();
//                }
//            }
//        });

        ImageView detailsButton = (ImageView) holder.mView.findViewById(R.id.detailsChevron);
        detailsButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra(PROFILE_NAME, mValues.get(position).getName());
                intent.putExtra(PROFILE_PICTURE, mValues.get(position).getPicture().getData().getSrc());
                intent.putExtra(SearchResultsActivity.DETAILS_ID, mValues.get(position).getId());
                intent.putExtra("PROFILE_OBJECT", gson.toJson(mValues.get(position)));
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
