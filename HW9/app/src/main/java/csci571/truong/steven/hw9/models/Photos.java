package csci571.truong.steven.hw9.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven on 4/25/2017.
 */

public class Photos {
    List<Picture> data;

    public void clearPhotos() {
        if(data == null) {
            data = new ArrayList<Picture>();
        }
        data.clear();
    }
    public void addNewPhoto(Picture p) {
        if(data == null) {
            data = new ArrayList<Picture>();
        }
        data.add(p);
    }
}
