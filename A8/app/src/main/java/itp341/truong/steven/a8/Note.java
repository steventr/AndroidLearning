package itp341.truong.steven.a8;

import java.util.Date;

/**
 * Created by Steven on 3/30/2016.
 */
public class Note {

    String title;
    String content;
    Date date;

    public Note() {
        title = "";
        content = "";
        date = new Date();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
}
