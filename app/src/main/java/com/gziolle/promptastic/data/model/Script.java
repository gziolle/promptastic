package com.gziolle.promptastic.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Script implements Parcelable {

    private String title;
    private String content;

    private Script(Parcel in) {
        this.title = in.readString();
        this.content = in.readString();
    }

    public Script(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getTitle());
        dest.writeString(getContent());
    }

    public static final Parcelable.Creator<Script> CREATOR = new Parcelable.Creator<Script>() {
        public Script createFromParcel(Parcel in) {
            return new Script(in);
        }

        public Script[] newArray(int size) {
            return new Script[size];
        }
    };
}
