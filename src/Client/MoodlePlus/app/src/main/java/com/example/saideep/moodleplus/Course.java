package com.example.saideep.moodleplus;

/**
 * Created by saideep on 18/2/16.
 * User defined class for storing information of a particular course
 *Implements Parcelable class which is used for passing data between activities
 */
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Course implements Parcelable{


    private String code;
    private String name;
    private String description;
    private int credits;
    private int id;
    private String l_t_p;

    public String getCode(){
        return  code;
    }

    public String getName()
    {
        return name;
    }
    public String getDescription()
    {
        return  description;
    }
    public int getCredits() {
        return credits;
    }

    public String getL_t_p() {
        return l_t_p;
    }

    public int getId()
    {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<Course> CREATOR = new Parcelable.Creator<Course>() {
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        public Course[] newArray(int size) {
            return new Course[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(credits);
        dest.writeInt(id);
        dest.writeString(l_t_p);
       ;

    }
    private Course(Parcel in) {
        this.code= in.readString();
        this.name = in.readString();
        this.description=in.readString();
        this.credits=in.readInt();
        this.id=in.readInt();
        this.l_t_p = in.readString();


        //this.selectedCourseID = in.readInt();
        //this.siteInfo = in.readParcelable(SiteInfo.class.getClassLoader());
        //in.readTypedList(this.courses, Course.CREATOR);
    }

    public Course(String code,String name,String description,int credits,int id,String l_t_p) {
      this.code=code;
      this.name=name;
      this.description=description;
      this.credits=credits;
      this.id=id;
      this.l_t_p=l_t_p;

    }
}
