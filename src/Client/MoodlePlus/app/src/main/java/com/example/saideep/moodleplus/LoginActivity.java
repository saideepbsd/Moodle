package com.example.saideep.moodleplus;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The initial activity of the application where the user is expected to login his credentials.If the credentials are incorrect, a toast message is displayed conveying the same. On successful login , the JSON response from the server is parsed and the necessary information is stored in an object of \textbf{User} class and this information is passed to the next activity
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    //public static String url = "http://192.168.0.102:8000"; //wifi
    public static String url = "http://192.168.43.75:8000"; //3g
    private EditText Username;
    private EditText Password;
    private Button LoginButton;
    private ProgressBar LoginprogressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Username = (EditText) findViewById(R.id.username);
        Password = (EditText) findViewById(R.id.password);
        LoginprogressBar = (ProgressBar) findViewById(R.id.login_progress);
        LoginButton = (Button) findViewById(R.id.sign_in_button);
        LoginButton.setOnClickListener(this);

    }




    public void authenticate(String usr,String pwd)
    {

        LoginprogressBar.setVisibility(View.VISIBLE);


        String Url = url + "/login.json?userid=" + usr + "&password=" + pwd;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                LoginprogressBar.setVisibility(View.GONE);

                try {
                    // Parsing json object response
                    // response will be a json object

                    String success = response.getString("success");
                    if(success.equals("false"))
                    {

                        Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        JSONObject user = response.getJSONObject("user");
                        String last_name = user.getString("last_name");
                        int id = user.getInt("id");
                        String first_name = user.getString("first_name");
                        String entry_no = user.getString("entry_no");
                        String email = user.getString("email");
                        String username = user.getString("username");
                        int type = user.getInt("type_");
                        String password = user.getString("password");

                        User sai = new User(last_name,first_name,entry_no,email,username,password,type,id);

                        Toast.makeText(LoginActivity.this, "Welcome " + first_name + " " + last_name, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getBaseContext(), CoursesList.class);
                        intent.putExtra("student",sai);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
                ;

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        requestQueue.add(jsonObjReq);


    }



    @Override
    public void onClick(View v) {


        final String usr = Username.getText().toString();
        final String pwd = Password.getText().toString();

        String EmptyField = "This is a required field!"; // Error message to be displayed in case of an empty field

        if (v == LoginButton) {


            if (usr.length() == 0) {
                Username.setError(EmptyField);
                Username.requestFocus();
            }
            else if (pwd.length() == 0) {
                Password.setError(EmptyField);
                Password.requestFocus();
            }
            else {
                authenticate(usr, pwd);

            }


        }
    }
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {  // if back button has been pressed once already
            super.onBackPressed();
            return;
        }

        //else

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        /*
         * reset the doubleBackToExitPressedOnce to false after 2 secs on inactivity
         *
         */

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }



}