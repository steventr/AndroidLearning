package csci571.truong.steven.hw9;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import csci571.truong.steven.hw9.models.SearchResultObject;
import csci571.truong.steven.hw9.models.SearchType;

public class SearchResultsActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, SearchFragmentPage.OnListFragmentInteractionListener {
    public static String DETAILS_ID = "DETAILS_ID";

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our mViewPager
    private ViewPager mViewPager;

    private SearchPageAdapter mAdapter;

    private ArrayList<SearchResultObject[]> mData;

    private SearchType currentPage;

    private int[] currentPageNum;

    private Button previousButton, nextButton;
    public SearchResultsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new SearchPageAdapter(getSupportFragmentManager(), this);

        setContentView(R.layout.activity_search_results);
        new SearchTask(this, mAdapter).execute(getIntent().getStringExtra(SearchActivity.SEARCH_QUERY));

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

        currentPage = SearchType.USER;

        previousButton = (Button) findViewById(R.id.previousButton);
        previousButton.setAlpha(.5f);
        previousButton.setEnabled(false);
        previousButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              List<SearchResultObject> resultsList = Arrays.asList(mData.get(SearchType.toInteger(currentPage)));
              int maxPages = (resultsList.size() / 10) + 1;

              int pageNumber = currentPageNum[SearchType.toInteger(currentPage)];
              if (pageNumber > 1) {
                  currentPageNum[SearchType.toInteger(currentPage)]--;
                  pageNumber = currentPageNum[SearchType.toInteger(currentPage)];
                  Log.d("TEST", "SHOWING INDEX: "+ (pageNumber - 1) * 10 + " TO INDEX: " + Math.min(pageNumber * 10 + 1, resultsList.size()));
                  mAdapter.updateResults(currentPage, resultsList.subList((pageNumber - 1) * 10, Math.min(pageNumber * 10 + 1, resultsList.size())));
                  mAdapter.notifyDataSetChanged();
                  setupTabIcons();
              }

              if (pageNumber == maxPages) {
                  nextButton.setAlpha(.5f);
                  nextButton.setEnabled(false);
              }
              else {
                  nextButton.setAlpha(1f);
                  nextButton.setEnabled(true);
              }

              if (pageNumber == 1) {
                  previousButton.setAlpha(.5f);
                  previousButton.setEnabled(false);
              }
              else {
                  previousButton.setAlpha(1f);
                  previousButton.setEnabled(true);
              }
          }
      });

        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pageNumber = currentPageNum[SearchType.toInteger(currentPage)];
                List<SearchResultObject> resultsList = Arrays.asList(mData.get(SearchType.toInteger(currentPage)));

                int maxPages = (resultsList.size() / 10) + 1;
                if (pageNumber < maxPages)  {
                    currentPageNum[SearchType.toInteger(currentPage)]++;
                    pageNumber = currentPageNum[SearchType.toInteger(currentPage)];
                    Log.d("TEST", "SHOWING INDEX: "+ (pageNumber - 1) * 10 + " TO INDEX: " + Math.min(pageNumber * 10 + 1, resultsList.size()));
                    mAdapter.updateResults(currentPage, resultsList.subList((pageNumber - 1) * 10, Math.min(pageNumber * 10 + 1, resultsList.size())));
                    mAdapter.notifyDataSetChanged();
                    setupTabIcons();
                }

                if (pageNumber >= maxPages) {
                    nextButton.setAlpha(.5f);
                    nextButton.setEnabled(false);
                }
                else {
                    nextButton.setAlpha(1f);
                    nextButton.setEnabled(true);
                }

                if (pageNumber == 1) {
                    previousButton.setAlpha(.5f);
                    previousButton.setEnabled(false);
                }
                else {
                    previousButton.setAlpha(1f);
                    previousButton.setEnabled(true);
                }
            }
        });

        setupTabIcons();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.d("TEST", "TAB SELECTED " + SearchType.fromInteger(tab.getPosition()).toString());
        mAdapter.setCurrentTab(SearchType.fromInteger(tab.getPosition()));
        mViewPager.setCurrentItem(tab.getPosition());
        currentPage = SearchType.fromInteger(tab.getPosition());


        //Update next/previous button states
        if (currentPageNum != null) {
            int pageNumber = currentPageNum[SearchType.toInteger(currentPage)];
            List<SearchResultObject> resultsList = Arrays.asList(mData.get(SearchType.toInteger(currentPage)));

            int maxPages = (resultsList.size() / 10) + 1;
            if (pageNumber == maxPages) {
                nextButton.setAlpha(.5f);
                nextButton.setEnabled(false);
            } else {
                nextButton.setAlpha(1f);
                nextButton.setEnabled(true);
            }

            if (pageNumber == 1) {
                previousButton.setAlpha(.5f);
                previousButton.setEnabled(false);
            } else {
                previousButton.setAlpha(1f);
                previousButton.setEnabled(true);
            }
        }
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
            tab.setCustomView(mAdapter.getTabView(i));
        }
    }

    public void setupPages(ArrayList<SearchResultObject[]> searchResultObjects) {
        currentPageNum = new int[5];
        for (int i = 0; i < 5; i++) {
            currentPageNum[i] = 1;
        }
        mData = searchResultObjects;
    }
}
