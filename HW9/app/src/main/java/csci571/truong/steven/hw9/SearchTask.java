package csci571.truong.steven.hw9;

import android.content.Context;
import android.os.AsyncTask;

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

import csci571.truong.steven.hw9.models.SearchResultObject;
import csci571.truong.steven.hw9.models.SearchType;

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

    private Context context;
    private SearchPageAdapter adapter;

    public SearchTask(Context context, SearchPageAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
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
        ArrayList<SearchResultObject[]> response = new ArrayList<SearchResultObject[]>();
        for (String json : responseJSONs) {
            JsonElement jelement = new JsonParser().parse(json);
            JsonObject jobject = jelement.getAsJsonObject();
            JsonArray resultsJsonArray = jobject.getAsJsonArray("data");
            String result = resultsJsonArray.toString();

            response.add(SearchResultObject.parseJSON(result));
        }

        adapter.updateResults(SearchType.USER, Arrays.asList(response.get(0)));
        adapter.updateResults(SearchType.PAGE, Arrays.asList(response.get(1)));
        adapter.updateResults(SearchType.EVENT, Arrays.asList(response.get(2)));
        adapter.updateResults(SearchType.PLACE, Arrays.asList(response.get(3)));
        adapter.updateResults(SearchType.GROUP, Arrays.asList(response.get(4)));
        adapter.notifyDataSetChanged();
        ((SearchResults) context).setupTabIcons();
    }
}


