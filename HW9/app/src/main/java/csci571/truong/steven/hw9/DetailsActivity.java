package csci571.truong.steven.hw9;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;

import csci571.truong.steven.hw9.dummy.DummyContent;
import csci571.truong.steven.hw9.models.Post;
import csci571.truong.steven.hw9.models.SearchResultObject;
import csci571.truong.steven.hw9.models.SearchType;

public class DetailsActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, PostsFragment.OnListFragmentInteractionListener, AlbumsFragment.OnListFragmentInteractionListener{
    //This is our tablayout
    private TabLayout tabLayout;

    //This is our mViewPager
    private ViewPager mViewPager;

    private DetailsPageAdapter mDetailsAdapter;
    private AlbumExpandableListAdapter mAlbumAdapter;

    private SearchResultObject mSearchResultObject;
    private CallbackManager mCallbackManager;

    public void setmCallbackManager(CallbackManager m) {
        mCallbackManager = m;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mDetailsAdapter = new DetailsPageAdapter(getSupportFragmentManager(), this);

        Gson gson = new Gson();
        mSearchResultObject = gson.fromJson(getIntent().getStringExtra("PROFILE_OBJECT"), SearchResultObject.class);

        new DetailsTask(this, mSearchResultObject.getType(), mDetailsAdapter, getIntent().getStringExtra(MyItemRecyclerViewAdapter.PROFILE_NAME), getIntent().getStringExtra(MyItemRecyclerViewAdapter.PROFILE_PICTURE)).execute(getIntent().getStringExtra(SearchResultsActivity.DETAILS_ID));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) findViewById(R.id.results_view);
        mViewPager.setAdapter(mDetailsAdapter);

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
            tab.setCustomView(mDetailsAdapter.getTabView(i));
        }
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details, menu);

        MenuItem favoriteMenuItem = menu.getItem(0);
        String detailsId = getIntent().getStringExtra(SearchResultsActivity.DETAILS_ID);
        if(detailsId == null) {
            detailsId = "";
        }

        if (Favorites.getInstance(this).exists(this,detailsId)) {
            favoriteMenuItem.setTitle("Remove from favorite");
        }
        else {
            favoriteMenuItem.setTitle("Add to favorite");
        }

        favoriteMenuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            private String mDetailsId;
            private Context mContext;
            private MenuItem mMenuItem;
            private SearchResultObject mSearchResultObject;

            private OnMenuItemClickListener init(String detailsId, Context c, MenuItem m, SearchResultObject searchResultObject) {
                mDetailsId = detailsId;
                mContext = c;
                mMenuItem = m;
                mSearchResultObject = searchResultObject;
                return this;
            }
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (mMenuItem.getTitle().equals("Add to favorite")) {
                    Favorites.getInstance(mContext).addFavorite(mContext, mSearchResultObject);
                    mMenuItem.setTitle("Remove from favorite");
                    Toast.makeText(mContext, "Added to favorites.", Toast.LENGTH_LONG).show();
                }
                else {
                    Favorites.getInstance(mContext).removeFavorite(mContext, mDetailsId);
                    mMenuItem.setTitle("Add to favorite");
                    Toast.makeText(mContext, "Removed from favorites.", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        }.init(detailsId, this, favoriteMenuItem, mSearchResultObject));

        MenuItem shareMenuItem = menu.getItem(1);

        shareMenuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            private String mDetailsId;
            private Context mContext;
            private MenuItem mMenuItem;
            private SearchResultObject mSearchResultObject;

            private OnMenuItemClickListener init(String detailsId, Context c, MenuItem m, SearchResultObject searchResultObject) {
                mDetailsId = detailsId;
                mContext = c;
                mMenuItem = m;
                mSearchResultObject = searchResultObject;
                return this;
            }
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(mContext, "Sharing " + mSearchResultObject.getName() + "!", Toast.LENGTH_LONG).show();

                CallbackManager x = CallbackManager.Factory.create();
                ((DetailsActivity) mContext).setmCallbackManager(x);

                ShareDialog y = new ShareDialog((Activity) mContext);
                y.registerCallback(x, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(mContext, "You shared this post.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });
                ShareLinkContent z = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://csci-571-162702.appspot.com/"))
                        .setContentTitle(mSearchResultObject.getName())
                        .setContentDescription("FB SEARCH FROM USC571")
                        .build();
                y.show((Activity)mContext, z);


                return false;
            }
        }.init(detailsId, this, favoriteMenuItem, mSearchResultObject));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data) {
        super.onActivityResult(requestcode,resultcode,data);
        mCallbackManager.onActivityResult(requestcode, resultcode,data);
    }
}
