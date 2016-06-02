package com.example.saideep.moodleplus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * This activity corresponds to the Welcome or Home screen where all the registered courses are listed.
 * On clicking any particular course, it redirects to CourseContent activity.
 */
public class CourseContent extends AppCompatActivity {

    Intent intent;
    Course course;
    TextView courseheading;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_content);

        intent = getIntent();
        course = (Course)intent.getParcelableExtra("course_info");
        courseheading = (TextView)findViewById(R.id.CourseName);
        courseheading.setText(course.getCode().toUpperCase()+" : "+course.getName().toUpperCase());

        listView = (ListView) findViewById(R.id.list);
        final String[] menu = {"Assignments","Grades","Threads"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CourseContent.this,
                android.R.layout.simple_list_item_1,android.R.id.text1,menu);
        // Populate the listview items

        listView.setAdapter(adapter);


        /*
         Define actions to be performed when a particular list item is clicked
         */

        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {

                switch (i)
                {
                    case 0: // if the  "Assignments" list item is pressed , redirect to ListofAssignments activity
                        Intent intent1 = new Intent(getBaseContext(),ListofAssignments.class);
                        intent1.putExtra("course_info", course);
                        startActivity(intent1);
                        break;
                    case 1:// if the  "Grades" list item is pressed , redirect to CourseGrades activity
                        Intent intent2 = new Intent(getBaseContext(),CourseGrades.class);
                        intent2.putExtra("course_info", course);
                        startActivity(intent2);
                        break;
                    case 2:// if the  "Threads" list item is pressed , redirect to ThreadsList activity
                        Intent intent3 = new Intent(getBaseContext(),ThreadsList.class);
                        intent3.putExtra("course_info", course);
                        startActivity(intent3);
                        break;
                    default:
                }





            }
        });
    }
}
