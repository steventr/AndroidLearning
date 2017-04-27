package csci571.truong.steven.hw9;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import csci571.truong.steven.hw9.models.Picture;
import csci571.truong.steven.hw9.models.SearchType;

/**
 * Created by Steven on 4/26/2017.
 */

public class HDImageTask  extends AsyncTask<String, String, String> {

    private String endpoint = "https://csci-571-162702.appspot.com/main.php?image_id=";

    private Context context;
    private Picture updateThis;

    public HDImageTask(Context context, Picture updateThis ) {
        this.context = context;
        this.updateThis = updateThis;
    }
    @Override
    protected String doInBackground(String... args) {
        String resultJSON = "";

        StringBuilder result = new StringBuilder();
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(endpoint + args[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            resultJSON = result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return resultJSON;
    }

    @Override
    protected void onPostExecute(String responseJSON) {
        JsonElement jelement = new JsonParser().parse(responseJSON);
        JsonObject jobject = jelement.getAsJsonObject();

        String newURL = jobject.get("data").getAsJsonObject().get("url").getAsString();
        updateThis.getData().setSrc(newURL);
    }
}
