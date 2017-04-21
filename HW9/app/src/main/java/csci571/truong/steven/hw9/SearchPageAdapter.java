package csci571.truong.steven.hw9;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import csci571.truong.steven.hw9.models.SearchResultObject;
import csci571.truong.steven.hw9.models.SearchType;

/**
 * Created by Steven on 4/17/2017.
 */

public class SearchPageAdapter extends FragmentStatePagerAdapter {

    private SearchType currentType;

    private ArrayList<ArrayList<SearchResultObject>> results;

    public SearchPageAdapter(FragmentManager fm) {
        super(fm);
        currentType = SearchType.USER;

        results = new ArrayList<ArrayList<SearchResultObject>>();

        for (int i = 0; i < 5; i++) {
            results.add(new ArrayList<SearchResultObject>());
        }
    }

    public void updateResults(SearchType type, List<SearchResultObject> newResults) {
        results.get(SearchType.toInteger(type)).clear();
        results.get(SearchType.toInteger(type)).addAll(newResults);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("TEST", "SEARCH PAGE ADAPTER GET ITEM " + position);
        return SearchFragmentPage.newInstance(results.get(SearchType.toInteger(currentType)));
    }

    @Override
    public int getCount() {
        return results.get(SearchType.toInteger(currentType)).size();
    }
}
