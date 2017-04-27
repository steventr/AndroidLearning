package csci571.truong.steven.hw9;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import csci571.truong.steven.hw9.AlbumsFragment.OnListFragmentInteractionListener;
import csci571.truong.steven.hw9.dummy.DummyContent.DummyItem;
import csci571.truong.steven.hw9.models.Album;
import csci571.truong.steven.hw9.models.FBObjectInstance;
import csci571.truong.steven.hw9.models.Post;

import java.util.Collections;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AlbumExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<Album> mAlbums;

    public AlbumExpandableListAdapter(List<Album> albums, Context context) {
        mAlbums = albums;
        mContext = context;
    }

    @Override
    public int getGroupCount() {
        if (mAlbums.size() == 0) {
            ((Activity) mContext).findViewById(R.id.noContent).setVisibility(View.VISIBLE);
        }

        return mAlbums.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mAlbums.get(groupPosition).getPhotos() != null) {
            return mAlbums.get(groupPosition).getPhotos().getNumPhotos();
        }
        else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mAlbums.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return  mAlbums.get(groupPosition).getPhotos().getPhotos().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.fragment_album_group, parent, false);
        }
        TextView albumTextView = (TextView) convertView.findViewById(R.id.albumHeaderText);
        albumTextView.setText(((Album) getGroup(groupPosition)).getName());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.fragment_album_item, parent, false);
        }

        Album showThisAlbum = (Album) getGroup(groupPosition);
        ImageView putPictureHere = (ImageView) convertView.findViewById(R.id.albumitemImageView);
        Picasso.with(mContext).load(showThisAlbum.getPhotos().getPhotos().get(childPosition).getData().getSrc()).into(putPictureHere);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void update(FBObjectInstance data){
        mAlbums.clear();
        Collections.addAll(mAlbums, data.getAlbums());


        if (mAlbums.size() == 0) {
            ((Activity) mContext).findViewById(R.id.noContent).setVisibility(View.VISIBLE);
        }
    }

    private class ViewHolder {

    }
}
