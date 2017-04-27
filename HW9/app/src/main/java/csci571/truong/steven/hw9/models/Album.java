package csci571.truong.steven.hw9.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Steven on 4/22/2017.
 */

public class Album {
    String id;
    String name;
    Photos photos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Photos getPhotos() {
        return photos;
    }

    public static Album[] parseJSON(String json) {
        Gson gson = new GsonBuilder().create();
        Album[] jsonAsAlbums = gson.fromJson(json, Album[].class);

        JsonElement jelement = new JsonParser().parse(json);
        JsonArray albumsJSONs = jelement.getAsJsonArray();

        for (int i = 0; i < albumsJSONs.size(); i++) {
            if ( albumsJSONs.get(i).getAsJsonObject().get("photos") != null) {
                JsonArray photos = albumsJSONs.get(i).getAsJsonObject().get("photos").getAsJsonObject().getAsJsonArray("data");
                jsonAsAlbums[i].getPhotos().clearPhotos();
                for (int j = 0; j < photos.size(); j++) {
                    JsonObject photo = photos.get(j).getAsJsonObject();
                    PictureData pd = gson.fromJson(photo.toString(), PictureData.class);
                    Picture picture = new Picture();
                    picture.setData(pd);
                    jsonAsAlbums[i].getPhotos().addNewPhoto(picture);
                }
            }
        }

        return jsonAsAlbums;
    }
}
