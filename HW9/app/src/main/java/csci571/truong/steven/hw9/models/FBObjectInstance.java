package csci571.truong.steven.hw9.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Steven on 4/22/2017.
 */

public class FBObjectInstance {
    String id;
    String name;
    Picture picture;
    @SerializedName("albums/data")
    Album[] albums;
    @SerializedName("posts/data")
    Post[] posts;

    public FBObjectInstance() {
        picture = new Picture();
        albums = new Album[0];
        posts = new Post[0];
    }
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

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public Album[] getAlbums() {
        return albums;
    }

    public void setAlbums(Album[] albums) {
        this.albums = albums;
    }

    public Post[] getPosts() {
        return posts;
    }

    public void setPosts(Post[] posts) {
        this.posts = posts;
    }

    public static FBObjectInstance parseJSON(String json) {
        Gson gson = new GsonBuilder().create();
        FBObjectInstance jsonAsFBObject = gson.fromJson(json, FBObjectInstance.class);
        return jsonAsFBObject;
    }
}
