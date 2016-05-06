package itp341.truong.steven.presence;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Steven on 4/17/2016.
 */
public class Member implements Parcelable {

    public String memberID;
    public String name;
    public int pin;
    public boolean isParent;
    public ArrayList<String> children;

    public Member(String id) {
        memberID = id;
    }

    public Member(Parcel in) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Member> CREATOR = new Parcelable.Creator<Member>() {
        @Override
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        @Override
        public Member[] newArray(int size) {
            return new Member[size];
        }
    };
}
