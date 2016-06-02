package com.example.saideep.moodleplus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

//Activity for displaying list of comments of a thread and also posting new ones
public class ThreadInfo extends AppCompatActivity implements View.OnClickListener{

    
    TextView posted_by,thread_title,created_at,last_update,description;
    EditText new_comment;
    Button submit;
    Intent intent;
    Thread thread;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_info);


        intent = getIntent();
        thread =intent.getParcelableExtra("thread_info");


        posted_by = (TextView) findViewById(R.id.posted_by);
        thread_title =(TextView)findViewById(R.id.posted_title);
        created_at =(TextView) findViewById(R.id.Created_At);
        last_update = (TextView) findViewById(R.id.Updated_At);
        description = (TextView) findViewById(R.id.description);

        new_comment =(EditText)findViewById(R.id.new_comment);


        posted_by.setText(""+getname(thread.getUser_id())+":");
        thread_title.setText(thread.getTitle());
        created_at.setText("Created at : "+thread.getCreated_at());
        last_update.setText("Last Updated at : "+thread.getUpdated_at());
        description.setText(thread.getDescription());

        submit = (Button) findViewById(R.id.post_button);
        submit.setOnClickListener(this);

      listView = (ListView) findViewById(R.id.list);

        Get_Comments task = new Get_Comments();

        try {
            ArrayList<Thread_Comments> comments_list= task.execute(thread.getId()).get();

            int n = comments_list.size();

            final String[] commentslist = new String[n];

            for(int i=0;i<n;i++)
            {
             commentslist[i]=comments_list.get(i).getPostedby()+" "+comments_list.get(i).getDescription()+" "+comments_list.get(i).getTimes_readable();
            }


            CustomListViewAdapter adapter = new CustomListViewAdapter(this,
                    R.layout.list_item_comment, comments_list);
            listView.setAdapter(adapter);



        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }




    }


    private class CustomListViewAdapter extends ArrayAdapter<Thread_Comments> {

        Context context;

        public CustomListViewAdapter(Context context, int resourceId,
                                     List<Thread_Comments> items) {
            super(context, resourceId, items);
            this.context = context;
        }

        /*private view holder class*/
        private class ViewHolder {
            TextView commenter;
            TextView desc;
            TextView times_readable;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            Thread_Comments rowItem = getItem(position);

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_comment, null);
                holder = new ViewHolder();
                holder.commenter = (TextView) convertView.findViewById(R.id.commmenter);
                holder.desc = (TextView) convertView.findViewById(R.id.desc);
                holder.times_readable = (TextView) convertView.findViewById(R.id.ago);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();

            holder.commenter.setText(rowItem.getPostedby());
            holder.desc.setText(rowItem.getDescription());
            holder.times_readable.setText(rowItem.getTimes_readable());


            return convertView;
        }
    }

    public String getname(int userid)
    {
        if(userid==1)
        return "John Doe";
        if(userid==2)
            return "Jasmeet Singh";
        if(userid==3)
            return "Abhishek Bansal";
        if(userid==4)
            return "Shubham Jindal";
        if(userid==6)
            return "Suresh Gupta";
        if(userid==7)
            return "Subodh Kumar";
        if(userid==5)
        return "Vinay Ribeiro";

        return "";

    }
    @Override
    public void onClick(View v) {

        final String comment = new_comment.getText().toString();

        String EmptyField = "This is a required field to post any comment!"; // Error message to be displayed in case of an empty field

        if (v == submit) {

            if (comment.length() == 0) {
                new_comment.setError(EmptyField);
                new_comment.requestFocus();
            }
            else
            {


                Post_Comment post = new Post_Comment();
                try {

                    boolean success = post.execute(thread.getId(),comment).get();

                    if(success)
                    {
                        Intent i = new Intent(getBaseContext(),ThreadInfo.class);
                        i.putExtra("thread_info",thread);
                        Toast.makeText(ThreadInfo.this, "Comment Posted Successfully!", Toast.LENGTH_SHORT).show();
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(ThreadInfo.this, "Comment could not be posted...Try again", Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }



            }


        }
    }

    //For sending GET request and parsing the obtained JSON response to store the information in an array of objects of Thread_Comments class
    private class Get_Comments extends AsyncTask<Integer, Void, ArrayList<Thread_Comments>> {

        @Override
        protected ArrayList<Thread_Comments> doInBackground(Integer... params) {

            ArrayList<Thread_Comments> var = new ArrayList<>();

            HttpURLConnection c = null;
            String Url = LoginActivity.url + "/threads/thread.json/" + params[0];
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
                    JSONArray jsonArray_comments = response.getJSONArray("comments");

                    for (int i = 0; i < jsonArray_comments.length(); i++) {

                        JSONObject comment = jsonArray_comments.getJSONObject(i);

                        int user_id = comment.getInt("user_id");
                        String description = comment.getString("description");
                        String created_at = comment.getString("created_at");
                        int thread_id = comment.getInt("thread_id");
                        int id = comment.getInt("id");

                        Thread_Comments t = new Thread_Comments(user_id, description,created_at, thread_id, id,"","");
                        var.add(t);
                    }

                    JSONArray times_readable = response.getJSONArray("times_readable");

                    for(int i=0;i<times_readable.length();i++)
                    {
                        String time = times_readable.getString(i);
                        Thread_Comments temp = var.get(i);
                        temp.setTimes_readable(time);
                        var.set(i,temp);
                    }

                    JSONArray comment_users = response.getJSONArray("comment_users");

                    for(int i=0;i<times_readable.length();i++)
                    {
                        JSONObject comment_user = comment_users.getJSONObject(i);
                        String lastname = comment_user.getString("last_name");
                        String fistname = comment_user.getString("first_name");

                        Thread_Comments temp = var.get(i);
                        temp.setPostedby(fistname + " " + lastname);;
                        var.set(i,temp);
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
    //For sending GET request to post a new comment
    private class Post_Comment extends AsyncTask<Object, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Object... params) {

            Boolean success = false;
            HttpURLConnection c = null;
            String Url = LoginActivity.url + "/threads/post_comment.json?thread_id=" + params[0] + "&description=" + params[1];
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
