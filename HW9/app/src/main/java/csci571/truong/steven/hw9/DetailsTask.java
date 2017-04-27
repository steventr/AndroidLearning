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
import java.util.ArrayList;

import csci571.truong.steven.hw9.models.Album;
import csci571.truong.steven.hw9.models.FBObjectInstance;
import csci571.truong.steven.hw9.models.Picture;
import csci571.truong.steven.hw9.models.Post;
import csci571.truong.steven.hw9.models.Posts;
import csci571.truong.steven.hw9.models.SearchType;

/**
 * Created by Steven on 4/22/2017.
 */

public class DetailsTask extends AsyncTask<String, String, String> {

    private String endpoint = "https://csci-571-162702.appspot.com/main.php?id=";

    private SearchType type;
    private DetailsPageAdapter adapter;
    private Context context;
    private String mProfileName, mProfilePicture;

    public DetailsTask(Context context, SearchType type, DetailsPageAdapter adapter, String profileName, String profilePicture) {
        this.context = context;
        this.type = type;
        this.adapter = adapter;
        mProfileName = profileName;
        mProfilePicture = profilePicture;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... args) {
        String resultJSON = "";

        StringBuilder result = new StringBuilder();
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(endpoint + args[0] + "&type=" + this.type.toString());
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
        FBObjectInstance details = FBObjectInstance.parseJSON(responseJSON);

        if (jobject.get("albums") != null) {
            String albumsJSON = jobject.get("albums").getAsJsonObject().get("data").getAsJsonArray().toString();
            Album[] parsedAlbums = Album.parseJSON(albumsJSON);

            for (Album a : parsedAlbums) {
                if (a.getPhotos() != null) {
                    for (Picture p : a.getPhotos().getPhotos()) {
                        new HDImageTask(context, p).execute(p.getData().getId());
                    }
                }
            }
            details.setAlbums(parsedAlbums);
        }
        else {
            details.setAlbums(new Album[0]);
        }

        if (jobject.get("posts") != null) {
            String postsJSON = jobject.get("posts").getAsJsonObject().get("data").getAsJsonArray().toString();
            Post[] parsedPosts = Posts.parseJSON(postsJSON);

            details.setPosts(parsedPosts);
        }
        else {
            details.setPosts(new Post[0]);
        }

        adapter.setDetails(details, mProfileName, mProfilePicture);
        adapter.notifyDataSetChanged();
        ((DetailsActivity) context).setupTabIcons();

    }
}
