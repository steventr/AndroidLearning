package csci571.truong.steven.hw9;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import csci571.truong.steven.hw9.models.SearchResultObject;

/**
 * Created by Steven on 4/17/2017.
 */

public class SearchTask extends AsyncTask<String, String, String> {

    private Context context;

    public SearchTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... args) {

        StringBuilder result = new StringBuilder();

        for (String arg : args) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(arg);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }

        return result.toString();
    }

    @Override
    protected void onPostExecute(String json) {
        JsonElement jelement = new JsonParser().parse(json);
        JsonObject jobject = jelement.getAsJsonObject();
        JsonArray resultsJsonArray = jobject.getAsJsonArray("data");
        String result = resultsJsonArray.toString();

        SearchResultObject[] test = SearchResultObject.parseJSON(result);
        for (SearchResultObject object : test) {
            Log.d("TEST", object.toString());
        }

        Intent intent = new Intent((Search) context, SearchResults.class);
        EditText editText = (EditText) ((Search) context).findViewById(R.id.searchInput);
        String message = editText.getText().toString();
        intent.putExtra(Search.SEARCH_QUERY, message);
        ((Search) context).startActivity(intent);
    }
}


