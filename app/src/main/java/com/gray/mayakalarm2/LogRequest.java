package com.gray.mayakalarm2;

import android.os.Parcel;
import android.os.Parcelable;

public final class LogRequest implements Parcelable {
    String msg;

    protected LogRequest(Parcel in) {
        msg = in.readString();
    }

    public static final Creator<LogRequest> CREATOR = new Creator<LogRequest>() {
        @Override
        public LogRequest createFromParcel(Parcel in) {
            return new LogRequest(in);
        }

        @Override
        public LogRequest[] newArray(int size) {
            return new LogRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(msg);
    }
}
