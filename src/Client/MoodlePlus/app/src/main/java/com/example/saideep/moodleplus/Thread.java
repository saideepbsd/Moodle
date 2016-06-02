package com.example.saideep.moodleplus;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by saideep on 22/2/16.
 * User defined class for storing details of Threads
 * Implements Parcelable class which is used for passing data between activities
 */
public class Thread implements  Parcelable{

    private int user_id;
    private String description;
    private String title;
    private String created_at;
    private int registered_course_id;
    private String updated_at;
    private int id;


    protected Thread(Parcel in) {
        user_id = in.readInt();
        description = in.readString();
        title = in.readString();
        created_at = in.readString();
        registered_course_id = in.readInt();
        updated_at = in.readString();
        id = in.readInt();
    }

    public static final Creator<Thread> CREATOR = new Creator<Thread>() {
        @Override
        public Thread createFromParcel(Parcel in) {
            return new Thread(in);
        }

        @Override
        public Thread[] newArray(int size) {
            return new Thread[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public int getRegistered_course_id() {
        return registered_course_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getTitle() {
        return title;
    }

    public Thread(int user_id,String description,String title,String created_at,int registered_course_id,String updated_at,int id)
    {
           this.user_id=user_id;
           this.description=description;
           this.title = title;
           this.created_at=created_at;
           this.registered_course_id=registered_course_id;
           this.updated_at = updated_at;
           this.id=id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(user_id);
        dest.writeString(description);
        dest.writeString(title);
        dest.writeString(created_at);
        dest.writeInt(registered_course_id);
        dest.writeString(updated_at);
        dest.writeInt(id);
    }
}
