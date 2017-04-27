package csci571.truong.steven.hw9;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import csci571.truong.steven.hw9.models.SearchResultObject;

/**
 * Created by Steven on 4/27/2017.
 */

public class Favorites {
    public static final String FAVORITES_KEY = "FAVORITES";
    private static Favorites favoriteSingleton;

    private List<SearchResultObject> favorites;
    private Gson mGson;

    public Favorites() {
        favorites = new ArrayList<SearchResultObject>();
        mGson = new Gson();
    }

    public static Favorites getInstance(Context c) {

        if (favoriteSingleton == null) {
            favoriteSingleton = new Favorites();
            String serializedFavorites = c.getSharedPreferences(FAVORITES_KEY, Context.MODE_PRIVATE).getString(FAVORITES_KEY, favoriteSingleton.mGson.toJson(new ArrayList<SearchResultObject>()));
            Type type = new TypeToken<List<SearchResultObject>>(){}.getType();
            favoriteSingleton.favorites = favoriteSingleton.mGson.fromJson(serializedFavorites, type);
        }

        return favoriteSingleton;
    }

    public void addFavorite(Context c, SearchResultObject newFavorite) {
        favorites.add(newFavorite);
        c.getSharedPreferences(FAVORITES_KEY, Context.MODE_PRIVATE).edit().putString(FAVORITES_KEY, favoriteSingleton.mGson.toJson(favorites)).commit();
    }

    public void removeFavorite(Context c, String id) {
        for (SearchResultObject fbObjectInstance : favorites) {
            if (fbObjectInstance.getId().equalsIgnoreCase(id)) {
                favorites.remove(fbObjectInstance);
                break;
            }
        }

        c.getSharedPreferences(FAVORITES_KEY, Context.MODE_PRIVATE).edit().putString(FAVORITES_KEY, favoriteSingleton.mGson.toJson(favorites)).commit();
    }

    public boolean exists(Context c, String id) {
        for (SearchResultObject fbObjectInstance : favorites) {
            if (fbObjectInstance.getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public List<SearchResultObject> getFavorites() {
        return favorites;
    }
}
