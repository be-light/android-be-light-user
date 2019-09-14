package com.example.a1117p.osam.user;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.simple.JSONObject;

public class ReciptListItem implements Parcelable {
    private String hostIdx,hostaddress,hostPostalCode,hostName,reciptNumber,checkin,checkOut,paid,hostUserPhoneNumber;
    ReciptListItem(JSONObject object){
        hostIdx = String.valueOf(object.get("hostIdx"));
        hostaddress = (String)object.get("hostaddress");
        hostPostalCode = (String)object.get("hostPostalCode");
        hostName = (String)object.get("hostName");
        reciptNumber = (String)object.get("reciptNumber");
        checkin = (String)object.get("checkin");
        checkOut = (String)object.get("checkOut");
        paid = (String)object.get("paid");
        hostUserPhoneNumber = (String)object.get("hostUserPhoneNumber");
    }

    protected ReciptListItem(Parcel in) {
        hostIdx = in.readString();
        hostaddress = in.readString();
        hostPostalCode = in.readString();
        hostName = in.readString();
        reciptNumber = in.readString();
        checkin = in.readString();
        checkOut = in.readString();
        paid = in.readString();
        hostUserPhoneNumber = in.readString();
    }

    public static final Creator<ReciptListItem> CREATOR = new Creator<ReciptListItem>() {
        @Override
        public ReciptListItem createFromParcel(Parcel in) {
            return new ReciptListItem(in);
        }

        @Override
        public ReciptListItem[] newArray(int size) {
            return new ReciptListItem[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    public String getHostIdx() {
        return hostIdx;
    }

    public String getHostaddress() {
        return hostaddress;
    }

    public String getHostPostalCode() {
        return hostPostalCode;
    }

    public String getHostName() {
        return hostName;
    }

    public String getReciptNumber() {
        return reciptNumber;
    }

    public String getCheckin() {
        return checkin;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public String getPaid() {
        return paid;
    }

    public String getHostUserPhoneNumber() {
        return hostUserPhoneNumber;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hostIdx);
        dest.writeString(hostaddress);
        dest.writeString(hostPostalCode);
        dest.writeString(hostName);
        dest.writeString(reciptNumber);
        dest.writeString(checkin);
        dest.writeString(checkOut);
        dest.writeString(paid);
        dest.writeString(hostUserPhoneNumber);
    }
}
