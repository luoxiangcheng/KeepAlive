package com.lxc.keepalive.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by luoxiangcheng on 2018/11/6 16:07
 */

public class Person implements Parcelable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person(String name) {
        this.name = name;
    }

    protected Person(Parcel in) {
        name = in.readString();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    /**
     * 如果要支持out或者inout的定向tag的话,需要写下面方法
     * 注意：此处的读值顺序应当是和writeToParcel()方法中一致的
     *
     * @param dest
     */
    public void readFromParcel(Parcel dest) {
        name = dest.readString();
    }

    @Override
    public String toString() {
        return "name = " + name;
    }
}
