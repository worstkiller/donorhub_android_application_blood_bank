package com.vikaskumar.materialdesign.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vikaskumar.materialdesign.R;
import com.vikaskumar.materialdesign.helper.SessionManager;
import com.vikaskumar.materialdesign.model.BloodGroupSelectorFilter;
import com.vikaskumar.materialdesign.model.DatePickerFragment;
import com.vikaskumar.materialdesign.network.AppConfig;
import com.vikaskumar.materialdesign.network.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vikas kumar on 10/21/2015.
 */
public class RegisterActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputphone;
    public static TextView inputAgeText;
    private ProgressDialog pDialog;
    private SessionManager session;
    private Spinner spinnerlastDonation, spinnerBlood;
    private String genderData;
    public static String ageData;
    private AutoCompleteTextView cityAutoText;
    private RadioGroup genderRadioGroup;
    private RadioButton genderMaleRadioButton, genderFemaleRadioButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initializing the controls
        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        inputAgeText = (TextView) findViewById(R.id.btnDateText);
        inputphone = (EditText) findViewById(R.id.phone);
        spinnerlastDonation = (Spinner) findViewById(R.id.lastdonate_spinner);
        spinnerBlood = (Spinner) findViewById(R.id.blood_spinner);
        cityAutoText = (AutoCompleteTextView) findViewById(R.id.city);
        genderRadioGroup = (RadioGroup) findViewById(R.id.radio_gender_group);
        genderMaleRadioButton = (RadioButton) findViewById(R.id.radio_male);
        genderFemaleRadioButton = (RadioButton) findViewById(R.id.radio_female);


        //setting spinners
        ArrayAdapter<CharSequence> adapter_spinner_donation = ArrayAdapter.createFromResource(this,
                R.array.last_duration_array, android.R.layout.simple_spinner_item);
        adapter_spinner_donation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerlastDonation.setAdapter(adapter_spinner_donation);

        ArrayAdapter<CharSequence> adapter_spinner_blood = ArrayAdapter.createFromResource(this,
                R.array.blood_array, android.R.layout.simple_spinner_item);
        adapter_spinner_blood.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBlood.setAdapter(adapter_spinner_blood);

        //setting the auto complete view
        final String[] cities = getResources().getStringArray(R.array.cities_array);
        ArrayAdapter<String> adapter_auto_city = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);
        cityAutoText.setAdapter(adapter_auto_city);

        //radio settings and listener
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int id = ((RadioGroup) findViewById(R.id.radio_gender_group)).getCheckedRadioButtonId();

                if (id == genderMaleRadioButton.getId()) {
                    genderData = "male";
                } else if (id == genderFemaleRadioButton.getId()) {
                    genderData = "female";
                } else {
                    genderData = "";
                }
                return;


            }
        });

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());


        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String nameData = inputFullName.getText().toString().trim();
                String emailData = inputEmail.getText().toString().trim();
                String passwordData = inputPassword.getText().toString().trim();
                String phoneData = inputphone.getText().toString().trim();
                String cityData = cityAutoText.getText().toString();
                String bloodData = String.valueOf(spinnerBlood.getSelectedItem());
                String lastDonationData = String.valueOf(spinnerlastDonation.getSelectedItem());

                //getting blood group from spinners and filtering it
                BloodGroupSelectorFilter.setBloodgroup(bloodData);
                bloodData=BloodGroupSelectorFilter.getBloodgroup();

                //checking for empty fields
                int flagCheck = 0;
                if (nameData.isEmpty() || nameData.length() <= 2) {
                    inputFullName.setError("Name is too Short");
                    flagCheck = 1;
                }
                if (emailData.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailData).matches()) {
                    inputEmail.setError("Email Invalid");
                    flagCheck = 1;
                }
                if (passwordData.isEmpty() || passwordData.length() < 4 || passwordData.length() > 16) {
                    inputPassword.setError("Password must be  atleast 4 characters");
                    flagCheck = 1;
                }
                if (phoneData.isEmpty() || phoneData.length() < 10) {
                    inputphone.setError("Enter a valid Phone Number");
                    flagCheck = 1;
                }
               /*if (ageData.isEmpty()) {

                   // inputAge.setError("Enter your Age");
                    flagCheck = 1;
                }*/
                if (cityData.isEmpty() || cityData.length() <= 2) {
                    cityAutoText.setError("PLease Enter a city you belong");
                    flagCheck = 1;
                }
                if (bloodData.isEmpty()) {

                    Toast.makeText(RegisterActivity.this, "Please Select your Blood Group", Toast.LENGTH_SHORT).show();
                    flagCheck = 1;
                }
                if (lastDonationData.isEmpty()) {

                    Toast.makeText(RegisterActivity.this, "Please Select your Blood Group", Toast.LENGTH_SHORT).show();
                    flagCheck = 1;
                } else if (flagCheck == 0) {
                    registerUser(nameData, emailData, passwordData, phoneData, cityData, ageData,
                            genderData, bloodData, lastDonationData);
                } else {
                    Toast.makeText(RegisterActivity.this, "Please Check Your Input Data", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        inputAgeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });



    }



    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     */
    private void registerUser(final String name, final String email,
                              final String password, final String phone, final String city,
                              final String age, final String gender, final String bloodGroup,
                              final String lastDonate) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering User...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    //boolean error = jObj.getBoolean("error");
                    String success = jObj.getString("success");
                    if (jObj.getString("success").equals("true")) {
                        //user successfully registered
                        session.setUserEmail(email);
                        session.setUserName(name);
                        Toast.makeText(getApplicationContext(), "Thank you , please Login to continue!"
                                , Toast.LENGTH_LONG).show();
                        // Launch login activity
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                "Connection Problem", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Can not connect to server", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("phone", phone);
                params.put("city", city);
                params.put("age", age);
                params.put("gender", gender);
                params.put("bloodgroup", bloodGroup);
                params.put("lastdonate", lastDonate);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}

