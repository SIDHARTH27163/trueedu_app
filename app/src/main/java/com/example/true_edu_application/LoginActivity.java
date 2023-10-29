package com.example.true_edu_application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class LoginActivity extends AppCompatActivity {
    private Button button_signin;
    private EditText editTextMobile, editTextPassword;
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String URL = "http://trueedu.appilogics.in/WebService.asmx";
    private static final String METHOD_NAME = "getstudent_login";
    private static final String SOAP_ACTION = "http://tempuri.org/getstudent_login";
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

      sessionManager = new SessionManager(this);

        Button button_signin = findViewById(R.id.button_signin);
        editTextMobile = findViewById(R.id.editTextMobile);
        editTextPassword = findViewById(R.id.editTextPassword);

        button_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = editTextMobile.getText().toString();
                String password = editTextPassword.getText().toString();
                loginUser(mobile, password);
            }
        });

        // Check if the user is already logged in
        if (sessionManager.isLoggedIn()) {
            // Redirect to the dashboard or next activity
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
        }
    }

    private void loginUser(String mobile, String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String password = editTextPassword.getText().toString();
                String mobile = editTextMobile.getText().toString();
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("password", password);
                request.addProperty("mobile", mobile);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                try {
                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                    runOnUiThread(new Runnable() {


                        @Override
                        public void run() {
                            try {
                                Log.d("responsed", response.toString());

                                // Parse the JSON response
                                JSONObject jsonResponse = new JSONObject(response.toString());
                                boolean status = jsonResponse.getBoolean("Status");

                                if (status) {
                                    JSONArray data = jsonResponse.getJSONArray("Data");
                                    if (data.length() > 0) {
                                        JSONObject userData = data.getJSONObject(0);
                                        String userEmail = userData.getString("UserEmail");
                                        String userID= userData.getString("UserId");
                                        String userfname= userData.getString("UserFirstName");
                                        String userlname= userData.getString("UserLastName");
                                        String usermobile =userData.getString("UserMobile");
                                        Log.d("success", "UserEmail: " + userEmail);
                                        Log.d("success", "UserId: " + userID);
                                        SharedPreferences sharedPreferences = getSharedPreferences("MySession", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        Toast.makeText(getApplicationContext(), "Hi Welcome Dear" +"" + userfname +"" +userlname +"" +"login succeeds", Toast.LENGTH_LONG).show();
                                        editor.putString("useremail", userEmail);
                                        editor.putString("usermobile", usermobile);
                                        editor.putString("userid", userID);
                                        editor.putString("username", userfname +""+ userlname);
                                        editor.apply();
                                        sessionManager.setLoggedIn(true);
                                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                        finish();
                                        // You can now use userEmail as needed, for example, disaying it in a TextView
                                        // textViewUserEmail.setText(userEmail);
                                    } else {
                                        Log.d("myTag", "No user data found in the response");
                                        Toast.makeText(getApplicationContext(), "Hi Welcome Dear Logged In Failed Due To No Data Found", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Log.d("myTag", "Login failed");
                                    Toast.makeText(getApplicationContext(), "Hi Welcome Dear Logged In Failed", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("myTag", "JSON parsing error");
                                Toast.makeText(getApplicationContext(), "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("myTag", "exception");
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                }

            }
        }).start();


    }
}
