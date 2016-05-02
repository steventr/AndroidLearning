package itp341.truong.steven.presence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven on 4/17/2016.
 */
public class Class {

    public String id;
    public String name;
    public String details;

    public boolean isSelected;

    public List<Member> students;

    public Class() {
        List<Member> students = new ArrayList<Member>();
    }

    public Class(String id, String details) {
        this.id = id;
        this.details = details;
        this.isSelected = false;

        List<Member> students = new ArrayList<Member>();
    }

}
