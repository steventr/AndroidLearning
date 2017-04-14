package itp341.truong.steven.presence;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import itp341.truong.steven.presence.Member;

public class Class implements Parcelable {

    public String id;
    public String name;
    public String details;

    public boolean isSelected;

    public ArrayList<String> meetingDays;
    public List<Member> students;

    public Class() {
        meetingDays = new ArrayList<String>();
        students = new ArrayList<Member>();
    }

    public Class(String id, String details) {
        this.id = id;
        this.details = details;
        this.isSelected = false;

        meetingDays = new ArrayList<String>();
        students = new ArrayList<Member>();
    }


    protected Class(Parcel in) {
        id = in.readString();
        name = in.readString();
        details = in.readString();
        isSelected = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            meetingDays = new ArrayList<String>();
            in.readList(meetingDays, String.class.getClassLoader());
        } else {
            meetingDays = null;
        }
        if (in.readByte() == 0x01) {
            students = new ArrayList<Member>();
            in.readList(students, Member.class.getClassLoader());
        } else {
            students = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(details);
        dest.writeByte((byte) (isSelected ? 0x01 : 0x00));
        if (meetingDays == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(meetingDays);
        }
        if (students == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(students);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Class> CREATOR = new Parcelable.Creator<Class>() {
        @Override
        public Class createFromParcel(Parcel in) {
            return new Class(in);
        }

        @Override
        public Class[] newArray(int size) {
            return new Class[size];
        }
    };
}