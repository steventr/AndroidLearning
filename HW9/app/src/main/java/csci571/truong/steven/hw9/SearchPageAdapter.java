package csci571.truong.steven.hw9;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import csci571.truong.steven.hw9.models.SearchResultObject;
import csci571.truong.steven.hw9.models.SearchType;

/**
 * Created by Steven on 4/17/2017.
 */

public class SearchPageAdapter extends FragmentStatePagerAdapter {
    private int[] iconResIds = {
        R.drawable.users, R.drawable.pages, R.drawable.events, R.drawable.places, R.drawable.groups
    };

    private SearchType currentType;

    private ArrayList<ArrayList<SearchResultObject>> results;

    private Context context;

    public SearchPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        currentType = SearchType.USER;

        results = new ArrayList<ArrayList<SearchResultObject>>();

        for (int i = 0; i < 5; i++) {
            results.add(new ArrayList<SearchResultObject>());
        }
    }

    public void setCurrentTab(SearchType type) {
        currentType = type;
    }

    public void updateResults(SearchType type, List<SearchResultObject> newResults) {
        results.get(SearchType.toInteger(type)).clear();
        results.get(SearchType.toInteger(type)).addAll(newResults);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("TEST", "GET ITEM POSITION: " + position);
        return SearchFragmentPage.newInstance(results.get(position), this);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tv = (TextView) v.findViewById(R.id.customTabText);

        String text;
        switch(position) {
            case 0:
                text = "Users";
                break;
            case 1:
                text = "Pages";
                break;
            case 2:
                text = "Events";
                break;
            case 3:
                text =  "Places";
                break;
            case 4:
                text = "Groups";
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
        return 5;
    }
}
