package csci571.truong.steven.hw9.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Steven on 4/17/2017.
 */
public class SearchResultObject {
    String id;
    String name;
    SearchType type;
    String pictureURL;

    public String toString() {
        return name + " " + id;
    }

    public static SearchResultObject[] parseJSON(String json) {
        Gson gson = new GsonBuilder().create();
        SearchResultObject[] jsonAsSearchResultObject = gson.fromJson(json, SearchResultObject[].class);
        return jsonAsSearchResultObject;
    }
}
