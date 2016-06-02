package com.example.saideep.moodleplus;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.View;
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
/*
This activity is for displaying the list of assignments corresponding to a particular course
 */
public class ListofAssignments extends AppCompatActivity {
    Intent intent;
    Course course;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listof_assignments);
        intent = getIntent();
        course = (Course)intent.getParcelableExtra("course_info");
        String course_code = course.getCode();


        Display_AssignmentsList task = new Display_AssignmentsList();
        try {

            final ArrayList<Assignment> assignments = task.execute(course_code).get();



            if(assignments.size()==0)
            {
                Toast.makeText(ListofAssignments.this, "No Assignments. Yay!!", Toast.LENGTH_SHORT).show();
            }
            else {

                // Populate the table layout with information from assignments array
                TableLayout t = (TableLayout) findViewById(R.id.table_main);

                TableRow row = new TableRow(this);

                TextView one = new TextView(this);
                one.setText(" S.no ");
                one.setGravity(Gravity.CENTER);
                one.setTextColor(Color.BLUE);
                row.addView(one);

                TextView two = new TextView(this);
                two.setText(" Name ");
                two.setTextColor(Color.BLUE);
                two.setGravity(Gravity.CENTER);
                row.addView(two);

                TextView three = new TextView(this);
                three.setText(" Deadline ");
                three.setTextColor(Color.BLUE);
                three.setGravity(Gravity.CENTER);
                row.addView(three);


                t.addView(row);

                for (int i = 0; i < assignments.size(); i++) {
                    TableRow newrow = new TableRow(this);

                    TextView newone = new TextView(this);
                    newone.setText("" + (i + 1) + " ");
                    newone.setGravity(Gravity.CENTER);
                    newone.setTextAppearance(this, android.R.style.TextAppearance_Medium);
                    newrow.addView(newone);

                    TextView newtwo = new TextView(this);
                    newtwo.setGravity(Gravity.CENTER);
                    newtwo.setTextAppearance(this, android.R.style.TextAppearance_Medium);
                    newtwo.setText(assignments.get(i).getName()+" ");
                    newtwo.setTextColor(Color.parseColor("#CC0033"));
                    newrow.addView(newtwo);

                    TextView newthree = new TextView(this);
                    newthree.setTextAppearance(this, android.R.style.TextAppearance_Medium);
                    newthree.setText(assignments.get(i).getDeadline());
                    newthree.setGravity(Gravity.CENTER);
                    newrow.addView(newthree);

                    t.addView(newrow);

                    newrow.setClickable(true);

                    final int finalI = i;
                    newrow.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            Assignment j = assignments.get(finalI);
                            Intent intent = new Intent(getBaseContext(),AssignmentInfo.class);
                            intent.putExtra("assignment", j);
                            startActivity(intent);
                        }
                    });

                }

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    //For sending GET request and parsing the obtained JSON response
    private class Display_AssignmentsList extends AsyncTask<String, Void,ArrayList<Assignment>> {

        @Override
        protected ArrayList<Assignment> doInBackground(String... params) {
            ;
            ArrayList<Assignment> var = new ArrayList<Assignment>();

            HttpURLConnection c = null;
            String Url = LoginActivity.url+"/courses/course.json/"+params[0]+"/assignments";
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
                    JSONArray jsonArray_grades  = response.getJSONArray("assignments");

                    for(int i=0;i<jsonArray_grades.length();i++)
                    {
                        JSONObject assignment = jsonArray_grades.getJSONObject(i);
                        String name = assignment.getString("name");
                        String created_at = assignment.getString("created_at");
                        int registered_course_id=assignment.getInt("registered_course_id");
                        int late_days_allowed = assignment.getInt("late_days_allowed");
                        int type_ = assignment.getInt("type_");
                        String deadline = assignment.getString("deadline");
                        int id = assignment.getInt("id");
                        String description=assignment.getString("description");
                        Assignment a = new Assignment(name,created_at,registered_course_id,late_days_allowed,type_,id,deadline,description);
                        var.add(a);
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
