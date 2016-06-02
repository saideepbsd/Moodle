package com.example.saideep.moodleplus;

/**
 * Created by saideep on 17/2/16.
 * User defined class for storing details of a user
 * Implements Parcelable class which is used for passing data between activities
 */

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {


    private String last_name;
    private String first_name;
    private String entry_no;
    private String email;
    private String username;
    private String password;
    private int type;
    private int id;


    private ArrayList<Course> courses = new ArrayList<Course>();

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public Course getCourse(int id) {
        for (Course c : courses) {
            if (c.getId() == id)
                return c;
        }
        return null;
    }

    public String getLast_name() {
        return last_name;
    }
    public String getFirst_name(){
        return  first_name;
    }
    public String getEntry_no()
    {
        return entry_no;
    }
    public String getEmail()
    {
        return  email;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getType()
    {
        return type;
    }
    public int getId()
    {
        return id;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addCourse(Course course)
    {
        courses.add(course);
    }



    @Override
    public int describeContents() {
        return 0;
    }

    // This is used to regenerate the object. All Parcelables must have a
    // CREATOR that implements these two methods
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // write the object's data to the passed-in Parcel
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(last_name);
        dest.writeString(first_name);
        dest.writeString(entry_no);
        dest.writeString(email);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeInt(type);
        dest.writeInt(id);
        dest.writeTypedList(courses);
        //dest.writeInt(selectedCourseID);
        //dest.writeParcelable(siteInfo, flags);
        //dest.writeTypedList(courses);
    }

    private User(Parcel in) {
        this.last_name= in.readString();
        this.first_name = in.readString();
        this.entry_no=in.readString();
        this.email=in.readString();
        this.username = in.readString();
        this.password = in.readString();
        this.type=in.readInt();
        this.id=in.readInt();
        in.readTypedList(this.courses, Course.CREATOR);

        //this.selectedCourseID = in.readInt();
        //this.siteInfo = in.readParcelable(SiteInfo.class.getClassLoader());
        //in.readTypedList(this.courses, Course.CREATOR);
    }

    public User(String last_name,String first_name,String entry_no,String email,String username,String password,int type,int id) {
        this.last_name=last_name;
        this.first_name=first_name;
        this.entry_no=entry_no;
        this.email=email;
        this.username=username;
        this.password=password;
        this.type=type;
        this.id=id;

    }
}

