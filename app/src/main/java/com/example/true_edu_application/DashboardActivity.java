package com.example.true_edu_application;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.GridView;


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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class DashboardActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_dashboard);
        sessionManager = new SessionManager(this);
        String course = "Computer Science Engineering";
        String sem = "1";
//for grid view starts


//        for grid view ens

        // Create an instance of SoapRequestAsyncTask with the provided values
        SoapRequestAsyncTask soapRequestAsyncTask = new SoapRequestAsyncTask(course, sem);

        // Execute the SOAP request
        soapRequestAsyncTask.execute();
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
                    Intent intent1 = new Intent(DashboardActivity.this, update.class);
                    startActivity(intent1);
                    // Handle item 1 click
                    Log.d("sucees", "itme 1");
                } else if (id == MENU_ITEM_2) {
                    // Handle item 2 click
//                    Intent intent2 = new Intent(MainActivity.this, YourNewActivity.class);
//                    startActivity(intent2);
                    Log.d("sucees", "itme 1");
                } else if (id == MENU_ITEM_3) {
                    // Handle item 3 click
//                    Intent intent3 = new Intent(MainActivity.this, AnotherNewActivity.class);
//                    startActivity(intent3);
                    Log.d("sucees", "itme 1");
                } else if (id == MENU_ITEM_4) {

                    startActivity(new Intent(DashboardActivity.this, student_packages.class));
                    finish();


                } else if (id == MENU_ITEM_5) {


                    startActivity(new Intent(DashboardActivity.this, selected_packages.class));
                    finish();


                } else if (id == MENU_ITEM_8) {
                    Toast.makeText(getApplicationContext(), "Hi  Dear" + "logged out successfully", Toast.LENGTH_LONG).show();
                    sessionManager.setLoggedIn(false);
                    startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
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


    public class SoapRequestAsyncTask extends AsyncTask<Void, Void, String> {
        //        private TextView editTextFname;
        private static final String NAMESPACE = "http://tempuri.org/";
        private static final String URL = "http://trueedu.appilogics.in/WebService.asmx";
        private static final String METHOD_NAME = "getpackage_by_coursenane_sem";

        private String course;
        private String sem;
        private String pkgname;

        public SoapRequestAsyncTask(String course, String sem) {
            this.course = course;
            this.sem = sem;

        }

        @Override
        protected String doInBackground(Void... voids) {
            String response = "";

            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                // Add parameters to the request
                PropertyInfo courseInfo = new PropertyInfo();
                courseInfo.setName("coursename");
                courseInfo.setValue(course);
                courseInfo.setType(String.class);
                request.addProperty(courseInfo);

                PropertyInfo semInfo = new PropertyInfo();
                semInfo.setName("sem");
                semInfo.setValue(sem);
                semInfo.setType(String.class);
                request.addProperty(semInfo);


                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(NAMESPACE + METHOD_NAME, envelope);

                Object responseObj = envelope.getResponse();
                if (responseObj instanceof SoapPrimitive) {
                    SoapPrimitive soapPrimitive = (SoapPrimitive) responseObj;
                    response = soapPrimitive.toString();
                } else if (responseObj instanceof SoapObject) {
                    // Handle the specific structure of your response
                    SoapObject soapObject = (SoapObject) responseObj;


                    Object resultObj = soapObject.getProperty("getpackage_by_coursenane_semResult");
                    if (resultObj instanceof SoapPrimitive) {
                        SoapPrimitive resultPrimitive = (SoapPrimitive) resultObj;
                        response = resultPrimitive.toString();
                    } else {
                        response = resultObj.toString();
                    }
                }
                Log.d("res", response);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("exception", e.getMessage());
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("responsed", result);
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
                    String packName = userData.getString("PackName");

                    // Add PackID and PackName to ArrayLists
                    packIDs.add(packID);
                    packNames.add(packName);
                }

                // Create an instance of the custom adapter
                CustomGridAdapter adapter = new CustomGridAdapter(DashboardActivity.this, packIDs, packNames);

                GridView gridView = findViewById(R.id.gridView);
                gridView.setAdapter(adapter);
                // Set the custom adapter as the adapter for the GridView
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                         int clickedIndex = (int) view.getTag();

//                             // Get the data associated with the clicked index
//                             JSONObject clickedData = data.getJSONObject(clickedIndex);
                        String clickedPackID = packIDs.get(position);
                        String clickedPackName = packNames.get(position);

                        Log.d("Clicked PackID", clickedPackID);
                        Log.d("Clicked PackName", clickedPackName);


                        // Execute the InsertDataTask with your data
                        for_submit(clickedPackID , clickedPackName);
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("exception", e.getMessage());
            }
        }
    }

    //for add submit
    protected void for_submit(String id ,String name ) {

        Log.d("Clicked PackID from function", id);
        Log.d("Clicked PackName function", name);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {


                    SharedPreferences sharedPreferences = getSharedPreferences("MySession", MODE_PRIVATE);

// Retrieve session values

                    String userId = sharedPreferences.getString("userid", "");
                    Date currentDate = new Date();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");

                    // Format the current date as a string
                    String formattedDate = dateFormat.format(currentDate);
                    Log.d("starts date" , String.valueOf(formattedDate));

                    SoapObject request = new SoapObject(NAMESPACE1, METHOD_NAME1);
                    request.addProperty("UserId", userId);
                    request.addProperty("bookid", "5");
                    request.addProperty("start_date",String.valueOf(formattedDate));
                    request.addProperty("end_date", String.valueOf(formattedDate));
                    request.addProperty("book_status", "0");
                    request.addProperty("book_pkg", id);
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);

                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL1);

                    androidHttpTransport.call(SOAP_ACTION1, envelope);
                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();




                    runOnUiThread(new Runnable() {


                        @Override
                        public void run() {
                            // Handle the response as needed

                            if(response!=null){

                                Toast.makeText(getApplicationContext(), "Hi  Dear" + "Pack Added To Cart Now Buy Your Pack", Toast.LENGTH_LONG).show();

                                startActivity(new Intent(DashboardActivity.this, selected_packages.class));
                                finish();
                            }else{
                                System.out.println("success from voud");
                            }

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("exception from submit" , e.toString());
                }
            }
        }).start();



    }

}