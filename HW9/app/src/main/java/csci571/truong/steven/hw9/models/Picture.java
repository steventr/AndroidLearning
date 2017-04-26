package csci571.truong.steven.hw9.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Steven on 4/21/2017.
 */

public class Picture {
    PictureData data;

    public PictureData getData() {
        return data;
    }

    public void setData(PictureData data) {
        this.data = data;
    }

    public static Picture parseJSON(String json) {
        Picture newPicture = new Picture();
        Gson gson = new GsonBuilder().create();
        PictureData jsonAsPictureData = gson.fromJson(json, PictureData.class);
        newPicture.setData(jsonAsPictureData);
        return newPicture;
    }

}
