package com.example.saideep.moodleplus;

import android.os.Parcelable;

/**
 * Created by saideep on 23/2/16.
 *User defined class for storing details of a particular thread including comments
 * Implements Parcelable class which is used for passing data between activities
 */

public class Thread_Comments {
    private int user_id;
    private String description;
    private String created_at;
    private int thread_id;
    private int id;
    private String times_readable;
    private String postedby;

    public String getPostedby() {
        return postedby;
    }

    public void setPostedby(String postedby) {
        this.postedby = postedby;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getTimes_readable() {
        return times_readable;
    }

    public int getThread_id() {
        return thread_id;
    }

    public void setTimes_readable(String times_readable) {
        this.times_readable = times_readable;
    }

    public Thread_Comments(int user_id,String description,String created_at,int thread_id,int id,String times_readable,String postedby)
    {
        this.user_id=user_id;
        this.description=description;
        this.created_at=created_at;
        this.thread_id=thread_id;
        this.id=id;
        this.times_readable=times_readable;
        this.postedby=postedby;
    }
}

