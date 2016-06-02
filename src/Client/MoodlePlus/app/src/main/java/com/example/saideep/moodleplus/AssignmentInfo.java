package com.example.saideep.moodleplus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
/**
 * Activity for displaying information of a particular assignment like Description,Deadline,Created At,Late Days allowed
 */
public class AssignmentInfo extends AppCompatActivity {


    Intent intent;
    TextView AssignmentTitle;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_info);
        intent = getIntent();

        Assignment assignment = intent.getParcelableExtra("assignment"); // get assignment information from the previous activity

        AssignmentTitle = (TextView)findViewById(R.id.AssignmentName);
        AssignmentTitle.setText(assignment.getName());

        listView = (ListView) findViewById(R.id.list);

        String description = String.valueOf(android.text.Html.fromHtml(assignment.getDescription()));
        String Created_at = "Created At : "+assignment.getCreated_at();
        String Deadline = "Deadline : " +assignment.getDeadline();
        String Late_Days = "Late Days Allowed : " + assignment.getLate_days_allowed();
        final String[] assignment_details ={description,Created_at,Deadline,Late_Days};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AssignmentInfo.this,
                android.R.layout.simple_list_item_1,android.R.id.text1,assignment_details);

        listView.setAdapter(adapter);




    }



}
