package com.example.saideep.moodleplus;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.widget.ArrayAdapter;
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
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Activity for displaying the grades of all registered courses in one screen
 */
public class GradesAll extends AppCompatActivity {

    private Intent intent;
    private User user;
    private TableLayout t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades_all);

        intent = getIntent();

        user = (User) intent.getParcelableExtra("student");

        Display_All_Grades task = new Display_All_Grades();

        try {

            ArrayList<Grades> gradeslist= task.execute().get();


            //http://stackoverflow.com/questions/18207470/adding-table-rows-dynamically-in-android

            if(gradeslist.size()==0)
            {
                Toast.makeText(GradesAll.this, "No Grades Awarded Yet!!!", Toast.LENGTH_SHORT).show();
            }
            else {
                //populate the table layout with information from gradeslist
                TableLayout t = (TableLayout) findViewById(R.id.table_main);

                TableRow row = new TableRow(this);

                TextView one = new TextView(this);
                one.setText(" S.no ");
                one.setTextColor(Color.BLUE);
                one.setGravity(Gravity.CENTER);
                row.addView(one);

                TextView two = new TextView(this);
                two.setText(" Course ");
                two.setTextColor(Color.BLUE);
                two.setGravity(Gravity.CENTER);
                row.addView(two);

                TextView three = new TextView(this);
                three.setText(" Grade Item ");
                three.setTextColor(Color.BLUE);
                three.setGravity(Gravity.CENTER);
                row.addView(three);

                TextView four = new TextView(this);
                four.setText(" Score ");
                four.setTextColor(Color.BLUE);
                four.setGravity(Gravity.CENTER);
                row.addView(four);

                TextView five = new TextView(this);
                five.setText(" Weight ");
                five.setTextColor(Color.BLUE);
                five.setGravity(Gravity.CENTER);
                row.addView(five);

                TextView six = new TextView(this);
                six.setText(" Absolute marks ");
                six.setGravity(Gravity.CENTER);
                six.setTextColor(Color.BLUE);
                row.addView(six);

                t.addView(row);

                for (int i = 0; i < gradeslist.size(); i++) {
                    TableRow newrow = new TableRow(this);

                    TextView newone = new TextView(this);
                    newone.setText(" " + (i+1));
                    newone.setGravity(Gravity.CENTER);
                    newrow.addView(newone);

                    TextView newtwo = new TextView(this);
                    int course_id = gradeslist.get(i).getRegistered_course_id();
                    newtwo.setText(user.getCourse(course_id).getCode().toUpperCase()+" ");
                    newtwo.setGravity(Gravity.CENTER);
                    newrow.addView(newtwo);

                    TextView newthree = new TextView(this);
                    newthree.setText(gradeslist.get(i).getName());
                    newthree.setGravity(Gravity.CENTER);
                    newrow.addView(newthree);

                    TextView newfour = new TextView(this);
                    double score = gradeslist.get(i).getScore();
                    double out_of = gradeslist.get(i).getOut_of();
                    newfour.setText(" " + score+"/"+out_of+" ");
                    newfour.setGravity(Gravity.CENTER);
                    newrow.addView(newfour);

                    TextView newfive = new TextView(this);
                    double weightage = gradeslist.get(i).getWeightage();
                    newfive.setText("" + weightage);
                    newfive.setGravity(Gravity.CENTER);
                    newrow.addView(newfive);

                    TextView newsix = new TextView(this);
                    double outof = gradeslist.get(i).getOut_of();
                    double absolute = (score / outof) * weightage;
                    absolute = (double) Math.round(absolute * 100.0) / 100.0;
                    newsix.setText("" + absolute);
                    newsix.setGravity(Gravity.CENTER);
                    newrow.addView(newsix);

                    t.addView(newrow);
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }




    // For sending GET request and parsing the obtained JSON response
    private class Display_All_Grades extends AsyncTask<Void, Void,ArrayList<Grades>> {

        @Override
        protected ArrayList<Grades> doInBackground(Void... voids) {

            ArrayList<Grades> var = new ArrayList<Grades>();

            HttpURLConnection c = null;
            String Url = LoginActivity.url+"/grades.json";
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

                    JSONArray jsonArray_grades  = response.getJSONArray("grades");
                    for(int i=0;i<jsonArray_grades.length();i++)
                    {
                        JSONObject grade = jsonArray_grades.getJSONObject(i);
                        double weightage = grade.getDouble("weightage");
                        int user_id = grade.getInt("user_id");
                        String name =grade.getString("name");
                        double out_of=grade.getDouble("out_of");
                        int registered_course_id=grade.getInt("registered_course_id");
                        double score=grade.getDouble("score");
                        int id=grade.getInt("id");
                        Grades g = new Grades(weightage,user_id,name,out_of,registered_course_id,score,id);
                        var.add(g);
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

