package com.example.saideep.moodleplus;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by saideep on 20/2/16.
 * User defined class for storing details of a particular assignment
 * Implements Parcelable class which is used for passing data between activities
 */


public class Assignment implements Parcelable{

    private String name;
    private String created_at;
    private int registered_course_id;
    private int late_days_allowed;
    private int type_;
    private int id;
    private String deadline;
    private String description;

    protected Assignment(Parcel in) {
        name = in.readString();
        created_at = in.readString();
        registered_course_id = in.readInt();
        late_days_allowed = in.readInt();
        type_ = in.readInt();
        id = in.readInt();
        deadline = in.readString();
        description = in.readString();
    }

    public static final Creator<Assignment> CREATOR = new Creator<Assignment>() {
        @Override
        public Assignment createFromParcel(Parcel in) {
            return new Assignment(in);
        }

        @Override
        public Assignment[] newArray(int size) {
            return new Assignment[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public int getLate_days_allowed() {
        return late_days_allowed;
    }

    public int getRegistered_course_id() {
        return registered_course_id;
    }

    public int getType_() {
        return type_;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getName() {
        return name;
    }
    public Assignment(String name,String created_at,int registered_course_id,int late_days_allowed,int type_,int id,String deadline,String description)
    {
        this.name=name;
        this.created_at=created_at;
        this.registered_course_id=registered_course_id;
        this.late_days_allowed=late_days_allowed;
        this.type_=type_;
        this.id=id;
        this.deadline=deadline;
        this.description=description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(created_at);
        dest.writeInt(registered_course_id);
        dest.writeInt(late_days_allowed);
        dest.writeInt(type_);
        dest.writeInt(id);
        dest.writeString(deadline);
        dest.writeString(description);
    }


}
