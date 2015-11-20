package com.vikaskumar.materialdesign.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vikaskumar.materialdesign.R;
import com.vikaskumar.materialdesign.helper.SessionManager;
import com.vikaskumar.materialdesign.network.AppConfig;
import com.vikaskumar.materialdesign.network.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class FriendsFragment extends Fragment {

    private static String TAG = FriendsFragment.class.getSimpleName();
    String switch_status;
    TextView status, msg;
    Switch aSwitch;
    SessionManager session;
    private ProgressDialog pDialog;


    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        status = (TextView) view.findViewById(R.id.switchStatus);
        msg = (TextView) view.findViewById(R.id.switch_msg);
        aSwitch = (Switch) view.findViewById(R.id.toggle_switch);

        // Session manager
        session = new SessionManager(getActivity());

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Boolean mySwitch = aSwitch.isChecked();
                String switch_value, email;
                email = session.getUserEmail();
                if (mySwitch == true) {
                    switch_value = "1";
                } else {
                    switch_value = "0";
                }
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Saving User Preferences...");
                makeUserProfile(switch_value, email);
            }
        });
        return view;
    }

    private void makeUserProfile(final String switch_value, final String email) {
        String tag_string_req = "req_profile";
        showpDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "profile Response: " + response);
                hidepDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String getSuccess = jObj.getString("success");
                    String getMessage = jObj.getString("message");
                    if (getSuccess == "1") {
                        // Launch login activity
                        Toast.makeText(getActivity().getApplicationContext(), getMessage, Toast.LENGTH_LONG).show();
                    } else {
                        // Error occurred . Get the error
                        // message
                        String errorMsg = jObj.getString("unexpected error occurred");
                        Toast.makeText(getActivity().getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Profile Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "privacy");
                params.put("flag", switch_value);
                params.put("email", email);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}//class end
