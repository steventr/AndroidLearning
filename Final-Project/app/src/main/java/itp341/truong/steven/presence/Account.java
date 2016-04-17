package itp341.truong.steven.presence;

import java.util.ArrayList;

/**
 * Created by Steven on 4/16/2016.
 */
public class Account {
    private String name;
    private String email;
    private String uid;
    private String provider;

    private ArrayList<String> classIDs;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getClassIDs() {
        return classIDs;
    }

    public void setClassIDs(ArrayList<String> classIDs) {
        this.classIDs = classIDs;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
