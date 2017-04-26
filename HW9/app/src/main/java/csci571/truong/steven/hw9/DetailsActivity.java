package csci571.truong.steven.hw9;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import csci571.truong.steven.hw9.dummy.DummyContent;
import csci571.truong.steven.hw9.models.Post;
import csci571.truong.steven.hw9.models.SearchType;

public class DetailsActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, PostsFragment.OnListFragmentInteractionListener, AlbumsFragment.OnListFragmentInteractionListener{
    //This is our tablayout
    private TabLayout tabLayout;

    //This is our mViewPager
    private ViewPager mViewPager;

    private DetailsPageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mAdapter = new DetailsPageAdapter(getSupportFragmentManager(), this);

        new DetailsTask(this, SearchType.USER, mAdapter, getIntent().getStringExtra(MyItemRecyclerViewAdapter.PROFILE_NAME), getIntent().getStringExtra(MyItemRecyclerViewAdapter.PROFILE_PICTURE)).execute(getIntent().getStringExtra(SearchResultsActivity.DETAILS_ID));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) findViewById(R.id.results_view);
        mViewPager.setAdapter(mAdapter);

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.results_selection);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setOnTabSelectedListener(this);

        //Adding the tabs using addTab() method
        tabLayout.setBackgroundColor(Color.WHITE);
        tabLayout.setTabTextColors(Color.BLACK, Color.BLACK);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        setupTabIcons();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.d("TEST", "TAB SELECTED " + SearchType.fromInteger(tab.getPosition()).toString());
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onListFragmentInteraction(Post item) {

    }

    public void setupTabIcons() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(mAdapter.getTabView(i));
        }
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
