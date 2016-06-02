package com.example.saideep.moodleplus;

/**
 * Created by saideep on 18/2/16.
 *  User defined class for storing details of grades
 * Implements Parcelable class which is used for passing data between activities
 */
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Grades implements Parcelable{


    private double weightage;
    private int user_id;
    private String name;
    private double out_of;
    private int registered_course_id;
    private double score;
    private int id;

    public double getWeightage(){
        return  weightage;
    }
    public String getName()
    {
        return name;
    }

    public int getUser_id()
    {
        return user_id;
    }
    public double getOut_of()
    {
        return out_of;
    }
    public int getRegistered_course_id()
    {
        return registered_course_id;
    }
    public double getScore()
    {
        return score;
    }
    public int getId()
    {
        return id;
    }


    @Override
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<Grades> CREATOR = new Parcelable.Creator<Grades>() {
        public Grades createFromParcel(Parcel in) {
            return new Grades(in);
        }

        public Grades[] newArray(int size) {
            return new Grades[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(weightage);
        dest.writeInt(user_id);
        dest.writeString(name);
        dest.writeDouble(out_of);
        dest.writeInt(registered_course_id);
        dest.writeDouble(score);
        dest.writeInt(id);

    }

    private Grades(Parcel in) {
        this.weightage= in.readDouble();
        this.user_id=in.readInt();
        this.name = in.readString();
        this.out_of=in.readDouble();
        this.registered_course_id=in.readInt();
        this.score = in.readDouble();
        this.id=in.readInt();

    }

    public Grades(double weightage,int user_id,String name,double out_of,int registered_course_id,double score,int id) {
        this.weightage= weightage;
        this.user_id=user_id;
        this.name = name;
        this.out_of=out_of;
        this.registered_course_id=registered_course_id;
        this.score = score;
        this.id=id;


    }
}
