package csci571.truong.steven.hw9;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import csci571.truong.steven.hw9.models.FBObjectInstance;
import csci571.truong.steven.hw9.models.SearchResultObject;
import csci571.truong.steven.hw9.models.SearchType;

/**
 * Created by Steven on 4/22/2017.
 */

public class DetailsPageAdapter extends FragmentStatePagerAdapter {
    private int[] iconResIds = { R.drawable.albums, R.drawable.posts
    };

    Context mContext;
    FBObjectInstance mDetails;
    String mProfileName, mProfilePicture;

    public DetailsPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    public void setDetails(FBObjectInstance details, String profileName, String profilePicture) {
        mDetails = details;
        mProfileName = profileName;
        mProfilePicture = profilePicture;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 : { return AlbumsFragment.newInstance(mDetails); }
            case 1 : { return PostsFragment.newInstance(mDetails, mProfileName, mProfilePicture ); }
            default :
                return AlbumsFragment.newInstance(mDetails);
        }
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
        TextView tv = (TextView) v.findViewById(R.id.customTabText);

        String text;
        switch(position) {
            case 0:
                text = "Albums";
                break;
            case 1:
                text = "Posts";
                break;
            default:
                text = "";
                break;
        }

        tv.setText(text);
        ImageView img = (ImageView) v.findViewById(R.id.customTabImage);
        img.setImageResource(iconResIds[position]);
        return v;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
