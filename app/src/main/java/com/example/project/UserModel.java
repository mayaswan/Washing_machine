package com.example.project;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable {

    private String nameString , urlAvatarSting;

    public UserModel() {
    }

    public UserModel(String nameString, String urlAvatarSting) {
        this.nameString = nameString;
        this.urlAvatarSting = urlAvatarSting;
    }


    protected UserModel(Parcel in) {
        nameString = in.readString();
        urlAvatarSting = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getNameString() {
        return nameString;
    }

    public void setNameString(String nameString) {
        this.nameString = nameString;
    }

    public String getUrlAvatarSting() {
        return urlAvatarSting;
    }

    public void setUrlAvatarSting(String urlAvatarSting) {
        this.urlAvatarSting = urlAvatarSting;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nameString);
        dest.writeString(urlAvatarSting);
    }
}
