package com.example.true_edu_application;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity {

    private EditText editTextFname, editTextLname, editTextEmail, editTextMobile;
    private Button buttonSubmit, buttonsignin;


    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String METHOD_NAME = "setstudent_registration";
    private static final String SOAP_ACTION = "http://tempuri.org/setstudent_registration";
    private static final String URL = "http://trueedu.appilogics.in/WebService.asmx";

    private SessionManager sessionManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(this);
        editTextFname = findViewById(R.id.editTextFname);
        editTextLname = findViewById(R.id.editTextLname);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextMobile = findViewById(R.id.editTextMobile);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonsignin = findViewById(R.id.buttonsignin);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = editTextFname.getText().toString();
                String lname = editTextLname.getText().toString();
                String email = editTextEmail.getText().toString();
                String mobile = editTextMobile.getText().toString();
                submitData();
            }
        });
        if (sessionManager.isLoggedIn()) {
            // Redirect to the dashboard or next activity
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            finish();
        }
        buttonsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                setContentView(R.layout.activity_login);
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });

    }

    private void submitData() {
//        final String fname = editTextFname.getText().toString();
//        final String lname = editTextLname.getText().toString();
//        final String email = editTextEmail.getText().toString();
//        final String mobile = editTextMobile.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String fname = editTextFname.getText().toString();
                String lname =editTextLname.getText().toString();
                String email = editTextEmail.getText().toString();
                String mobile = editTextMobile.getText().toString();

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("Fname", fname);
                request.addProperty("Lname", lname);
                request.addProperty("email", email);
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
                            // Handle the response as needed

                            if(response!=null){
                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(i);
                                Toast.makeText(getApplicationContext(),"dear"+fname+"Registration Successfull ", Toast.LENGTH_SHORT).show();
                                System.out.println(response);
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
}
