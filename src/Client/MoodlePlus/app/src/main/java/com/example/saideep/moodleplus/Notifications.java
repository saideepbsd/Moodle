package com.example.saideep.moodleplus;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
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
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;


// Activity for displayng list of notifications
public class Notifications extends AppCompatActivity {

    private Intent intent;
    private User user;
    private TableLayout t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Display_Notifications task = new Display_Notifications();
        try {
            ArrayList<Notification> notifications= task.execute().get();

            if(notifications.size()==0)
            {
                Toast.makeText(Notifications.this, "No Notifications Yet!!!", Toast.LENGTH_LONG).show();
            }
            else {

                TableLayout t = (TableLayout) findViewById(R.id.table_main);

                TableRow row = new TableRow(this);

                TextView one = new TextView(this);
                one.setText(" S.no ");
                one.setGravity(Gravity.CENTER);
                one.setTextColor(Color.BLUE);
                row.addView(one);

                TextView two = new TextView(this);
                two.setText(" Notification ");
                two.setTextColor(Color.BLUE);
                two.setGravity(Gravity.CENTER);
                row.addView(two);

                TextView three = new TextView(this);
                three.setText(" Time ");
                three.setTextColor(Color.BLUE);
                three.setGravity(Gravity.CENTER);
                row.addView(three);


                t.addView(row);

                for (int i = 0; i < notifications.size(); i++) {
                    TableRow newrow = new TableRow(this);

                    TextView newone = new TextView(this);
                    newone.setText("" + (i+1)+" ");
                    newone.setGravity(Gravity.CENTER);
                    newrow.addView(newone);

                    TextView newtwo = new TextView(this);

                    newtwo.setText(android.text.Html.fromHtml(notifications.get(i).getDescription())+"  ");
                    newtwo.setGravity(Gravity.CENTER);
                    newrow.addView(newtwo);

                    TextView newthree = new TextView(this);
                    newthree.setText(notifications.get(i).getCreated_at());
                    newthree.setGravity(Gravity.CENTER);
                    newrow.addView(newthree);

                    t.addView(newrow);
                }
            }




        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //For sending GET request and parsing the obtained JSON response
    private class Display_Notifications extends AsyncTask<Void, Void,ArrayList<Notification>> {

        @Override
        protected ArrayList<Notification> doInBackground(Void... voids) {

            ArrayList<Notification> var = new ArrayList<Notification>();
            Notification n = null;
            HttpURLConnection c = null;
            String Url = LoginActivity.url + "/notifications.json";
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
                    JSONArray jsonArray  = response.getJSONArray("notifications");

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject notification = jsonArray.getJSONObject(i);

                        int user_id = notification.getInt("user_id");
                        String description = notification.getString("description");
                        int is_seen = notification.getInt("is_seen");
                        String created_at = notification.getString("created_at");
                        int id = notification.getInt("id");
                        n = new Notification(user_id, description, is_seen, created_at, id);
                        var.add(n);

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
}
