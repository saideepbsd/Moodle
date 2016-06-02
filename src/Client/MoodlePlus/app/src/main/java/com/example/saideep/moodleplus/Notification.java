package com.example.saideep.moodleplus;

/**
 * Created by saideep on 20/2/16.
 * User defined class for storing details of notifications
 * Implements Parcelable class which is used for passing data between activities
 */
public class Notification {

    private int user_id;
    private String description;
    private int is_seen;
    private String created_at;
    private int id;

    public int getId() {
        return id;
    }

    public int getIs_seen() {
        return is_seen;
    }

    public String getCreated_at() {
        return created_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getDescription() {
        return description;
    }


    public Notification(int user_id,String description,int is_seen,String created_at,int id) {

        this.user_id=user_id;
        this.description = description;
        this.is_seen=is_seen;
        this.created_at=created_at;
        this.id=id;

    }

}
