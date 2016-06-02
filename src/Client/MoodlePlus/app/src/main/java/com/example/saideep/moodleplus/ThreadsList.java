package com.example.saideep.moodleplus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
Activity for displaying list of thread and also posting new ones
 */
public class ThreadsList extends AppCompatActivity implements View.OnClickListener {
    Intent intent;
    Course course;
    ListView listView;
    TextView courseheading;
    EditText title, description;
    Button start_new_thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threads_list);

        intent = getIntent();
        course = intent.getParcelableExtra("course_info");
        String course_code = course.getCode();

        courseheading = (TextView) findViewById(R.id.CourseName);
        courseheading.setText(course.getCode().toUpperCase() + " : " + "Threads");

        title = (EditText) findViewById(R.id.thread_title);
        description = (EditText) findViewById(R.id.thread_description);

        start_new_thread = (Button) findViewById(R.id.post_button);
        start_new_thread.setOnClickListener(this);

        Display_Threads task = new Display_Threads();
        try {

            final ArrayList<Thread> threadslist = task.execute(course_code).get();



            if (threadslist.size() == 0) {
                Toast.makeText(ThreadsList.this, "No Threads To Display!!!", Toast.LENGTH_LONG).show();
            } else {

                listView = (ListView) findViewById(R.id.list);
                int size = threadslist.size();
                String[] threads = new String[size];
                for (int i = 0; i < size; i++) {
                    threads[i] = + (i + 1) + "  " + threadslist.get(i).getTitle();
                }



                CustomListViewAdapter adapter = new CustomListViewAdapter(this,
                        R.layout.list_item_thread, threadslist);
                listView.setAdapter(adapter);

                listView.setAdapter(adapter);
                listView.setClickable(true);

                listView.setOnItemClickListener(new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View v, int i, long l) {

                       Intent intent2 = new Intent(getBaseContext(),
                                ThreadInfo.class);

                       Thread th = threadslist.get(i);
                       intent2.putExtra("thread_info",th);
                       startActivity(intent2);



                    }
                });

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
   //http://theopentutorials.com/tutorials/android/listview/android-custom-listview-with-image-and-text-using-arrayadapter/
    private class CustomListViewAdapter extends ArrayAdapter<Thread> {

        Context context;

        public CustomListViewAdapter(Context context, int resourceId,
                                     List<Thread> items) {
            super(context, resourceId, items);
            this.context = context;
        }

        /*private view holder class*/
        private class ViewHolder {
            TextView Sno;
            TextView Title;
            TextView Updatedon;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            Thread rowItem = getItem(position);

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_thread, null);
                holder = new ViewHolder();
                holder.Sno = (TextView) convertView.findViewById(R.id.Sno);
                holder.Title = (TextView) convertView.findViewById(R.id.title);
                holder.Updatedon = (TextView) convertView.findViewById(R.id.Updated_on);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.Sno.setText(""+(position+1));
            holder.Title.setText(rowItem.getTitle());
            holder.Updatedon.setText("Updated at " +rowItem.getUpdated_at());


            return convertView;
        }
    }

    @Override
    public void onClick(View v) {
        final String thread_title = title.getText().toString();
        final String thread_description = description.getText().toString();

        String EmptyField = "This is a required field!"; // Error message to be displayed in case of an empty field

        if (v == start_new_thread) {


            if (thread_title.length() == 0) {
                title.setError(EmptyField);
                title.requestFocus();
            } else if (thread_description.length() == 0) {
                description.setError(EmptyField);
                description.requestFocus();
            } else {

                Post_Thread post = new Post_Thread();
                Boolean success = false;
                try {
                    success = post.execute(thread_title, thread_description, course.getCode()).get();
                    if(success) {
                        Intent i = new Intent(getBaseContext(), ThreadsList.class);
                        i.putExtra("course_info", course);
                        Toast.makeText(ThreadsList.this, "Posted Successfully!", Toast.LENGTH_SHORT).show();
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(ThreadsList.this, "Ooops!!Could not be posted!", Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }


        }
    }

    //For sending GET request and parsing the obtained JSON response to store the information in an array of objects of Thread class
    private class Display_Threads extends AsyncTask<String, Void, ArrayList<Thread>> {

        @Override
        protected ArrayList<Thread> doInBackground(String... params) {
            ;
            ArrayList<Thread> var = new ArrayList<Thread>();

            HttpURLConnection c = null;
            String Url = LoginActivity.url + "/courses/course.json/" + params[0] + "/threads";
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
                    JSONArray jsonArray_threads = response.getJSONArray("course_threads");

                    for (int i = 0; i < jsonArray_threads.length(); i++) {
                        JSONObject thread = jsonArray_threads.getJSONObject(i);

                        int user_id = thread.getInt("user_id");
                        String description = thread.getString("description");
                        String title = thread.getString("title");
                        String created_at = thread.getString("created_at");
                        int registered_course_id = thread.getInt("registered_course_id");
                        String updated_at = thread.getString("updated_at");
                        int id = thread.getInt("id");

                        Thread t = new Thread(user_id, description, title, created_at, registered_course_id, updated_at, id);
                        var.add(t);
                    }

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
            return var;
        }



    }

//For posting new threads
    private class Post_Thread extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            Boolean success = false;
            HttpURLConnection c = null;
            String Url = LoginActivity.url + "/threads/new.json?title=" + params[0] + "&description=" + params[1] + "&course_code=" + params[2];
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
                    String succ = response.getString("success");
                    if (succ.equals("true"))
                        success = true;

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
            return success;
        }

    }

}
