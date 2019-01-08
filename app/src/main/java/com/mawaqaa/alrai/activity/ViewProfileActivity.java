package com.mawaqaa.alrai.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mawaqaa.alrai.R;
import com.mawaqaa.alrai.other.PrefManage;
import com.mawaqaa.alrai.other.urlClass;

import org.json.JSONObject;

public class ViewProfileActivity extends Activity {

    TextView nameTXT, emailTXT;
    String userId;
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        initView();
        userId = new PrefManage(ViewProfileActivity.this).getUserDetails();

        progressBar = ProgressDialog.show(ViewProfileActivity.this, "", "Please Wait ...", true, false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.putOpt("UserId", userId);
                    jsonObject.putOpt("SecurityKey", urlClass.SecurityKey);
                    makeVolleyRequest(urlClass.getUserDetails, jsonObject);
                } catch (Exception xx) {
                    xx.toString();
                }
            }
        }).start();


    }

    private void initView() {
        nameTXT = (TextView) findViewById(R.id.view_profile_name_TXT);
        emailTXT = (TextView) findViewById(R.id.view_profile_email_TXT);
    }

    private void makeVolleyRequest(String url, JSONObject jsonObject) {

        RequestQueue queue = Volley.newRequestQueue(ViewProfileActivity.this);
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response", response.toString());
                processData(response);
                progressBar.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.d("error", error.toString());
                ViewProfileActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ViewProfileActivity.this, getApplicationContext().getString(R.string.toast_error_message), Toast.LENGTH_LONG).show();

                    }
                });
                progressBar.dismiss();

            }
        });


        jr.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 20,
                2,
                2));
        queue.add(jr);
    }

    private void processData(JSONObject response) {
        try {
            String UserId = response.getString("UserId");
            String Message = response.getString("Message");
            String email = response.getString("Email");
            String profileName = response.getString("ProfileName");

            nameTXT.setText(profileName);
            emailTXT.setText(email);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
