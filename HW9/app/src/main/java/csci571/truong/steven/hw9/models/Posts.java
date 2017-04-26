package csci571.truong.steven.hw9.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Steven on 4/25/2017.
 */

public class Posts {
    Post[] data;

    public static Post[] parseJSON(String json) {
        Gson gson = new GsonBuilder().create();
        Post[] jsonAsAlbums = gson.fromJson(json, Post[].class);
        return jsonAsAlbums;
    }
}
