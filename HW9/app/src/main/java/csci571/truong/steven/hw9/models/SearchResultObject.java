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
    Picture picture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SearchType getType() {
        return type;
    }

    public void setType(SearchType type) {
        this.type = type;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public String toString() {
        return name + " " + id;
    }

    public static SearchResultObject[] parseJSON(String json) {
        Gson gson = new GsonBuilder().create();
        SearchResultObject[] jsonAsSearchResultObject = gson.fromJson(json, SearchResultObject[].class);
        return jsonAsSearchResultObject;
    }


    public static class SearchResultObjectView {
        public final String id;
        public final String content;
        public final String details;

        public SearchResultObjectView(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
