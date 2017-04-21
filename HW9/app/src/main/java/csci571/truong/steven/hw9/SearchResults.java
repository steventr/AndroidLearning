package csci571.truong.steven.hw9;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import csci571.truong.steven.hw9.models.SearchResultObject;
import csci571.truong.steven.hw9.models.SearchType;

public class SearchResults extends AppCompatActivity implements TabLayout.OnTabSelectedListener, SearchFragmentPage.OnListFragmentInteractionListener{
    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    private SearchPageAdapter adapter;

    public SearchResults() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SearchPageAdapter(getSupportFragmentManager(), this);

        setContentView(R.layout.activity_search_results);
        new SearchTask(this, adapter).execute(getIntent().getStringExtra(Search.SEARCH_QUERY));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.results_view);
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.results_selection);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(this);

        //Adding the tabs using addTab() method
        tabLayout.setBackgroundColor(Color.WHITE);
        tabLayout.setTabTextColors(Color.BLACK, Color.BLACK);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        setupTabIcons();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.d("TEST", "TAB SELECTED " + tab.getPosition());
        adapter.setCurrentTab(SearchType.fromInteger(tab.getPosition()));
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onListFragmentInteraction(SearchResultObject searchResult) {

    }

    public void setupTabIcons() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }
    }
}
