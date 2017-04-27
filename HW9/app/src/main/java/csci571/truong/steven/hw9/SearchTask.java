package csci571.truong.steven.hw9;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import csci571.truong.steven.hw9.models.PagingObject;
import csci571.truong.steven.hw9.models.SearchResultObject;
import csci571.truong.steven.hw9.models.SearchType;

import static android.content.Context.LOCATION_SERVICE;
import static android.location.LocationManager.GPS_PROVIDER;

/**
 * Created by Steven on 4/17/2017.
 */

public class SearchTask extends AsyncTask<String, String, String[]> {

    private String[] endpoints = {"https://csci-571-162702.appspot.com/main.php?type=user&query=",
            "https://csci-571-162702.appspot.com/main.php?type=page&query=",
            "https://csci-571-162702.appspot.com/main.php?type=event&query=",
            "https://csci-571-162702.appspot.com/main.php?type=place&query=",
            "https://csci-571-162702.appspot.com/main.php?type=group&query="
    };

    private String location;
    private Context context;
    private SearchPageAdapter adapter;

    public SearchTask(Context context, SearchPageAdapter adapter, String l) {
        this.context = context;
        this.adapter = adapter;
        location = l;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String[] doInBackground(String... args) {
        String[] resultJSONs = new String[5];

        for (int i = 0; i < 5; i++) {
            StringBuilder result = new StringBuilder();
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(endpoints[i] + args[0]);
                if (i == 3 && !location.isEmpty()) {
                    url = new URL(endpoints[i] + args[0] + "&location=" + location);
                }
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                resultJSONs[i] = result.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }

        return resultJSONs;
    }

    @Override
    protected void onPostExecute(String[] responseJSONs) {
        ArrayList<SearchResultObject[]> searchResultObjects = new ArrayList<SearchResultObject[]>();
        ArrayList<PagingObject> pagingObjects = new ArrayList<PagingObject>();

        int j = 0;
        for (String json : responseJSONs) {
            JsonElement jelement = new JsonParser().parse(json);
            JsonObject jobject = jelement.getAsJsonObject();
            JsonArray resultsJsonArray = jobject.getAsJsonArray("data");
            String resultJSON = resultsJsonArray.toString();
            SearchResultObject[] results = SearchResultObject.parseJSON(resultJSON);
            for (SearchResultObject searchResultObject : results) {
                searchResultObject.setType(SearchType.fromInteger(j));
            }
            searchResultObjects.add(results);
            j++;
        }

        for (int i = 0; i < 5; i++) {
            List<SearchResultObject> resultsList = Arrays.asList(searchResultObjects.get(i));
            if ( resultsList.size() > 0) {
                Log.d("TEST", "UPDATING " + SearchType.fromInteger(i).toString());
                adapter.updateResults(SearchType.fromInteger(i), resultsList.subList(0, Math.min(11, resultsList.size())));
            }
        }
        adapter.notifyDataSetChanged();
        ((SearchResultsActivity) context).setupPages(searchResultObjects);
        ((SearchResultsActivity) context).setupTabIcons();
    }
}

