package csci571.truong.steven.hw9;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by Steven on 4/17/2017.
 */

public class SearchPageAdapter extends FragmentStatePagerAdapter {

    public SearchPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("TEST", "SEARCH PAGE ADAPTER GET ITEM " + position);
        return new SearchFragmentPage();
    }

    @Override
    public int getCount() {
        return 0;
    }
}
