package csci571.truong.steven.hw9.models;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Steven on 4/22/2017.
 */

public class PagingObject {
    String next;
    String previous;

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public static PagingObject parseJSON(String json) {
        Gson gson = new GsonBuilder().create();
        PagingObject jsonAsPagingObject = gson.fromJson(json, PagingObject.class);

        if (jsonAsPagingObject.getNext() != null) {
            Log.d("TEST", jsonAsPagingObject.getNext());
        }

        if (jsonAsPagingObject.getPrevious() != null) {
            Log.d("TEST", jsonAsPagingObject.getPrevious());
        }

        return jsonAsPagingObject;
    }

}
