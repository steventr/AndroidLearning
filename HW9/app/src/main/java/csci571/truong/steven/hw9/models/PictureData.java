package csci571.truong.steven.hw9.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Steven on 4/21/2017.
 */

public class PictureData {
    String name;
    String id;
    @SerializedName(value="src", alternate={"url", "picture"})
    String src;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String url) {
        this.src = url;
    }

}
