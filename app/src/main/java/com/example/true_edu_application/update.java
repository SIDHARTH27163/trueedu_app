package com.example.true_edu_application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class update extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private static final int MENU_ITEM_1 = R.id.nav_item1;
    private static final int MENU_ITEM_2 = R.id.nav_item2;
    private static final int MENU_ITEM_3 = R.id.nav_item3;
    private static final int MENU_ITEM_4 = R.id.nav_item4;
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String URL = "http://trueedu.appilogics.in/WebService.asmx";
    private static final String SOAP_ACTION = "http://tempuri.org/updatestudent_profile";
    private static final String METHOD_NAME = "updatestudent_profile";
    private static final String SOAP_ACTION1 = "http://tempuri.org/get_institution";
    private static final String METHOD_NAME1 = "get_institution";
    private EditText etFname, etLname, etPhoto, etBranch, etInstitution;
    private Button btnUpdate;
    private SessionManager sessionManager;
    private Spinner spinnerInstitution , spinnerSemester;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etLname);
        etPhoto = findViewById(R.id.etPhoto);
        etBranch = findViewById(R.id.etBranch);
        spinnerSemester = findViewById(R.id.spinnerSemester);

        btnUpdate = findViewById(R.id.btnUpdate);

//        for draer


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
//        for dwrawer ends1

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.semester_items, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the Spinner
        spinnerInstitution = findViewById(R.id.spinnerInstitution);
        spinnerSemester.setAdapter(adapter);
        new CallWebService().execute();
        sessionManager = new SessionManager(this);
        // Set an item selection listener to handle selections
        spinnerSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item (semester)
                String selectedSemester = parent.getItemAtPosition(position).toString();
                // You can use selectedSemester as needed.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected (if needed)
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedSemester = spinnerSemester.getSelectedItem().toString();
                String selectedinstitute = spinnerInstitution.getSelectedItem().toString();

                Log.d("Selected Semester", selectedSemester);
                Log.d("Selected Institute", selectedinstitute);
                updateStudentProfile();
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                // Handle item clicks here
                if (id == MENU_ITEM_1) {
                    Intent intent1 = new Intent(update.this, update.class);
                    startActivity(intent1);
                    // Handle item 1 click
                    Log.d("sucees" , "itme 1");
                } else if (id == MENU_ITEM_2) {
                    Intent intent1 = new Intent(update.this, DashboardActivity.class);
                    startActivity(intent1);
                    // Handle item 2 click
//                    Intent intent2 = new Intent(MainActivity.this, YourNewActivity.class);
//                    startActivity(intent2);
                    Log.d("sucees" , "itme 1");
                } else if (id == MENU_ITEM_3) {
                    // Handle item 3 click
//                    Intent intent3 = new Intent(MainActivity.this, AnotherNewActivity.class);
//                    startActivity(intent3);
                    Log.d("sucees" , "itme 1");
                }else if (id== MENU_ITEM_4) {
                    Toast.makeText(getApplicationContext(), "Hi  Dear"+"logged out successfully", Toast.LENGTH_LONG).show();
                    sessionManager.setLoggedIn(false);
                    startActivity(new Intent(update.this, LoginActivity.class));
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
            Log.d("drwaer" , "backpressed");
        } else {
            super.onBackPressed();
        }
    }
//    update codes
private void updateStudentProfile() {

    new Thread(new Runnable() {
        @Override
        public void run() {
            SharedPreferences sharedPreferences = getSharedPreferences("MySession", MODE_PRIVATE);

// Retrieve session values

            String userId = sharedPreferences.getString("userid", "");
            String fname = etFname.getText().toString();
            String lname =etLname.getText().toString();
            String photo = etPhoto.getText().toString();
            String branch = etBranch.getText().toString();
            String selectedSemester = spinnerSemester.getSelectedItem().toString();
            String selectedinstitute = spinnerInstitution.getSelectedItem().toString();
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("Fname", fname);
            request.addProperty("Lname", lname);
            request.addProperty("photo", photo);
            request.addProperty("branch", branch);
            request.addProperty("semester", 1);
            request.addProperty("institution", selectedinstitute);
            request.addProperty("id", userId);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {

                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();


                System.out.println(response);
                Log.d("response" , response.toString());
                runOnUiThread(new Runnable() {


                    @Override
                    public void run() {
                        // Handle the response as needed

                        if(response!=null){
                            SharedPreferences sharedPreferences = getSharedPreferences("MySession", MODE_PRIVATE);

// Retrieve session values
                            String username = sharedPreferences.getString("username", "");
                            String userId = sharedPreferences.getString("userid", "");

                            Toast.makeText(getApplicationContext(),"dear"+username+""+"Profile Updated Successfully ", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(update.this, DashboardActivity.class));
                            finish();
                        }else{
                            System.out.println("success from voud");
                        }


                    }
                });
            } catch (Exception e) {

                e.printStackTrace();
            }

        }
    }).start();
}
//    update ends
//fetch institution
private class CallWebService extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... params) {
        try {
            // Create a SOAP request
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);

            // Create SOAP envelope
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            // Create HTTP transport
            HttpTransportSE httpTransport = new HttpTransportSE(URL);
            httpTransport.debug = true;

            // Call the web service
            httpTransport.call(SOAP_ACTION1, envelope);

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
            return "Error: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
Log.d("sucess response" , result);
        // Display the result in the Android Studio console (for debugging)
        try {
            // Parse the JSON data from the result string
            JSONObject jsonResponse = new JSONObject(result);
            JSONArray data = jsonResponse.getJSONArray("Data");

            List<String> instituteNames = new ArrayList<>();

            for (int i = 0; i < data.length(); i++) {
                JSONObject userData = data.getJSONObject(i);
                String name = userData.getString("InstituteName");
Log.d("instituename" ,name);
                instituteNames.add(name);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(update.this, android.R.layout.simple_spinner_item, instituteNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerInstitution.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("exception", e.getMessage());
        }

    }

}

}

//    ftech institutiton ends


