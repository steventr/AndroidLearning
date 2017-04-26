package csci571.truong.steven.hw9.models;

/**
 * Created by Steven on 4/22/2017.
 */

public class Post {
    String message;
    String created_time;
    String id;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
