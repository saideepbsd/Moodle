package com.example.saideep.moodleplus;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CoursesList extends AppCompatActivity implements View.OnClickListener {


    Intent intent;
    User user;
    ListView listView;
    private Button logout;
    private Button myCourses;
    private Button notifications;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_list);
        intent = getIntent();

        user = (User) intent.getParcelableExtra("student");

        myCourses = (Button) findViewById(R.id.button);
        logout = (Button) findViewById(R.id.button2);
        notifications = (Button)findViewById(R.id.button3);


        logout.setOnClickListener(this);
        myCourses.setOnClickListener(this);
        notifications.setOnClickListener(this);


       Display_Courses task = new Display_Courses();

        try {
            user.setCourses(task.execute().get());
            final ArrayList<Course> c = user.getCourses();

            String[] courselist = new String[c.size()];
            int a = 0;
            for (Course course :c) {
                courselist[a] = course.getCode().toUpperCase()+" : "+course.getName().toUpperCase();
                a++;
            }

            listView = (ListView) findViewById(R.id.list);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(CoursesList.this,
                    android.R.layout.simple_list_item_1,android.R.id.text1,courselist);

            listView.setAdapter(adapter);
            listView.setClickable(true);

            listView.setOnItemClickListener(new ListView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int i, long l) {

                    Intent intent = new Intent(getBaseContext(),
                            CourseContent.class);

                    Course course = c.get(i);
                    intent.putExtra("course_info",course);
                    startActivity(intent);
                }
            });

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }






    @Override
    public void onClick(View v) {
             if(v==myCourses)
             {
                 Intent intent = new Intent(getBaseContext(), GradesAll.class);
                 intent.putExtra("student",user);
                 startActivity(intent);
             }
             else if(v==logout)
             {
                 Logout logout = new Logout();
                 logout.execute();

             }
             else if(v==notifications)
             {
                 Intent intent = new Intent(getBaseContext(), Notifications.class);
                 intent.putExtra("student", user);
                 startActivity(intent);
             }

    }




    private class Display_Courses extends AsyncTask<Void, Void,ArrayList<Course>> {

        @Override
        protected ArrayList<Course> doInBackground(Void... voids) {
            ArrayList<Course> var = new ArrayList<Course>();
            HttpURLConnection c = null;
            String Url = LoginActivity.url + "/courses/list.json";
            try {
                BufferedReader rd = null;
                StringBuilder sb = null;
                String line = null;
                URL u = new URL(Url);
                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                c.connect();
                rd = new BufferedReader(new InputStreamReader(c.getInputStream()));
                sb = new StringBuilder();

                while ((line = rd.readLine()) != null) {

                    sb.append(line + '\n');

                }

                String s = sb.toString();
                try {
                    JSONObject response = new JSONObject(s);
                    String current_year = response.getString("current_year");

                    JSONArray jsonArray = response.getJSONArray("courses");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject course = jsonArray.getJSONObject(i);

                        String code = course.getString("code");
                        String name = course.getString("name");
                        String description = course.getString("description");
                        int credits = course.getInt("credits");
                        int id = course.getInt("id");
                        String l_t_p = course.getString("l_t_p");

                        Course co = new Course(code, name, description, credits, id, l_t_p);
                        var.add(co);
                    }
                    String current_sem = response.getString("current_sem");

                    return var;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (c != null) {
                    try {
                        c.disconnect();
                    } catch (Exception ex) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            // Toast.makeText(MainActivity.this, "dddd", Toast.LENGTH_LONG).show();
            return var;
        }

        public void onPostExecute(ArrayList<Course> c) {

        }

    }

    private class Logout extends AsyncTask<Void, Void,String> {

        @Override
        protected String doInBackground(Void... voids) {

            HttpURLConnection c = null;

            String Url = LoginActivity.url+"/logout.json";

            try {
                BufferedReader rd = null;
                StringBuilder sb = null;
                String line = null;
                URL u = new URL(Url);
                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                c.connect();
                rd = new BufferedReader(new InputStreamReader(c.getInputStream()));
                sb = new StringBuilder();

                while ((line = rd.readLine()) != null) {

                    sb.append(line + '\n');

                }

                String s = sb.toString();
                try {
                    JSONObject response = new JSONObject(s);
                    String noti_count = response.getString("noti_count");

                    return noti_count;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (c != null) {
                    try {
                        c.disconnect();
                    } catch (Exception ex) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return null;
        }

        public void onPostExecute(String c) {
            if(c==null)
            {
                Toast.makeText(CoursesList.this, "Error logging out!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(CoursesList.this, "Successfully logged out!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        }

    }
}




