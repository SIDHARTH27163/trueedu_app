package com.example.true_edu_application;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import android.os.AsyncTask;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class selected_packages extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private static final int MENU_ITEM_1 = R.id.nav_item1;
    private static final int MENU_ITEM_2 = R.id.nav_item2;
    private static final int MENU_ITEM_3 = R.id.nav_item3;
    private static final int MENU_ITEM_4 = R.id.nav_item4;
    private static final int MENU_ITEM_5 = R.id.nav_item5;

    private static final int MENU_ITEM_8 = R.id.nav_item8;
    private SessionManager sessionManager;


    private static final String NAMESPACE1 = "http://tempuri.org/";
    private static final String METHOD_NAME1 = "set_bookPackage";
    private static final String SOAP_ACTION1 = "http://tempuri.org/set_bookPackage";
    private static final String URL1 = "http://trueedu.appilogics.in/WebService.asmx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_packages);
        sessionManager = new SessionManager(this);

//for grid view starts



//        for grid view ens

        // Create an instance of SoapRequestAsyncTask with the provided values

        // Execute the SOAP request
        SharedPreferences sharedPreferences = getSharedPreferences("MySession", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userid", "");
         Log.d("user id" , userId);
        // Make the SOAP API request
        MyAsyncTask myAsyncTask = new MyAsyncTask(userId);
        myAsyncTask.execute();
//for grid view starts


//        for grid view ens

        // Create an instance of SoapRequestAsyncTask with the provided values
//        DashboardActivity.SoapRequestAsyncTask soapRequestAsyncTask = new DashboardActivity.SoapRequestAsyncTask(course, sem);

        // Execute the SOAP request
//        soapRequestAsyncTask.execute();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Toggle the navigation drawer when the toolbar icon is clicked
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Handle navigation item clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                // Handle item clicks here
                if (id == MENU_ITEM_1) {

                    // Handle item 1 click
                    Log.d("sucees", "itme 1");
                } else if (id == MENU_ITEM_2) {
                    // Handle item 2 click
//                    Intent intent2 = new Intent(MainActivity.this, YourNewActivity.class);
//                    startActivity(intent2);
                    startActivity(new Intent(selected_packages.this, DashboardActivity.class));
                    finish();

                } else if (id == MENU_ITEM_3) {
                    // Handle item 3 click
//                    Intent intent3 = new Intent(MainActivity.this, AnotherNewActivity.class);
//                    startActivity(intent3);
                    Log.d("sucees", "itme 1");
                } else if (id == MENU_ITEM_4) {

                    startActivity(new Intent(selected_packages.this, student_packages.class));
                    finish();


                } else if (id == MENU_ITEM_5) {


                    finish();


                } else if (id == MENU_ITEM_8) {
                    Toast.makeText(getApplicationContext(), "Hi  Dear" + "logged out successfully", Toast.LENGTH_LONG).show();
                    sessionManager.setLoggedIn(false);

                    finish();

                }
                // Add more if-else blocks for other menu items as needed

                // Close the drawer
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }


        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//get data starts
public class MyAsyncTask extends AsyncTask<Void, Void, String> {
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String URL = "http://trueedu.appilogics.in/WebService.asmx";
    private static final String METHOD_NAME = "get_pckgbyuserid_sts_false";
    private static final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
    private String userId;

    public MyAsyncTask(String userId) {
        this.userId = userId;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            // Add parameters to the request
            PropertyInfo userIdProperty = new PropertyInfo();
            userIdProperty.setName("userid");
            userIdProperty.setValue(userId);
            userIdProperty.setType(String.class);
            request.addProperty(userIdProperty);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Create HTTP transport
            HttpTransportSE httpTransport = new HttpTransportSE(URL);
            httpTransport.debug = true;

            // Call the web service
            httpTransport.call(SOAP_ACTION, envelope);

            // Get the SOAP response
            Object response = envelope.getResponse();

            // Check the type of the response
            if (response instanceof SoapPrimitive) {
                // It's a primitive type (e.g., string or integer)
                return response.toString();
            } else if (response instanceof SoapObject) {
                // It's a complex object, you may need to further parse it
                SoapObject soapObject = (SoapObject) response;
                // Process the soapObject as needed
                return soapObject.toString();
            } else {
                return "Unexpected response type";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        // Update the UI or log the result
        if (result != null) {
            try {
                // Parse the JSON data from the result string
                JSONObject jsonResponse = new JSONObject(result);

                // Assuming the response is an array of objects under a key "Data"
                JSONArray data = jsonResponse.getJSONArray("Data");

                ArrayList<String> packIDs = new ArrayList<>();
                ArrayList<String> packNames = new ArrayList<>();

                for (int i = 0; i < data.length(); i++) {
                    JSONObject userData = data.getJSONObject(i);

                    // Extract the PackID and PackName from each JSON object
                    String packID = userData.getString("PackID");
                    String packName = userData.getString("PackPrice");
Log.d("sucees" , String.valueOf(userData));
                    // Add PackID and PackName to ArrayLists
                    packIDs.add(packID);
                    packNames.add(packName);
                }

                // Create an instance of the custom adapter
                CustomGridAdapter adapter = new CustomGridAdapter(selected_packages.this, packIDs, packNames);

                GridView gridView = findViewById(R.id.gridView);
                gridView.setAdapter(adapter);
                // Set the custom adapter as the adapter for the GridView



            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("exception", e.getMessage());
            }
            // You can update the UI or perform further processing here
        } else {
            Log.d("failed", "not fetched");
        }
    }
}
//    get data ends
}