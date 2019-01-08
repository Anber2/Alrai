package com.mawaqaa.alrai.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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

public class EditProfileActivity extends Activity {

    EditText emailEXT, nameETXT;
    Button submitEditProfile;
    String userId;
    ProgressDialog progressBar;
    Context context;
    Animation animShke;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        context = getApplicationContext();
        initView();
        userId = new PrefManage(EditProfileActivity.this).getUserDetails();

        progressBar = ProgressDialog.show(EditProfileActivity.this, "", "Please Wait ...", true, false);
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


        submitEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkRule()) {
                    progressBar = ProgressDialog.show(EditProfileActivity.this, "", "Please Wait ...", true, false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.putOpt("ProfileName", nameETXT.getText().toString().trim());
                                jsonObject.putOpt("UserId", userId);
                                jsonObject.putOpt("SecurityKey", urlClass.SecurityKey);
                                makeVolleyRequest2(urlClass.updateProfile, jsonObject);
                            } catch (Exception xx) {
                                xx.toString();
                            }
                        }
                    }).start();
                }
            }
        });

    }


    private void initView() {
        animShke = AnimationUtils.loadAnimation(context, R.anim.shake);
        nameETXT = (EditText) findViewById(R.id.edit_profile_name_etxt);
        emailEXT = (EditText) findViewById(R.id.edit_profile_email_etxt);
        submitEditProfile = (Button) findViewById(R.id.edit_profile_submit_btn);
    }

    private boolean checkRule() {
        if (nameETXT.getText().toString().trim().equals("")) {
            Toast.makeText(context, context.getString(R.string.name_validation), Toast.LENGTH_SHORT).show();
            nameETXT.startAnimation(animShke);
            return false;
        }
        if (emailEXT.getText().toString().trim().equals("")) {
            Toast.makeText(context, context.getString(R.string.email_validation), Toast.LENGTH_SHORT).show();
            emailEXT.startAnimation(animShke);
            return false;
        }
        return true;
    }

    private void makeVolleyRequest(String url, JSONObject jsonObject) {

        RequestQueue queue = Volley.newRequestQueue(EditProfileActivity.this);
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
                EditProfileActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(EditProfileActivity.this, context.getString(R.string.toast_error_message), Toast.LENGTH_LONG).show();

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

            nameETXT.setText(profileName);
            emailEXT.setText(email);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void makeVolleyRequest2(String url, JSONObject jsonObject) {
        RequestQueue queue = Volley.newRequestQueue(EditProfileActivity.this);
        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response", response.toString());
                processData2(response);
                progressBar.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.d("error", error.toString());
                EditProfileActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(EditProfileActivity.this, context.getString(R.string.toast_error_message), Toast.LENGTH_LONG).show();

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

    private void processData2(JSONObject response) {
        try {
            boolean status = response.getBoolean("Status");
            String Message = response.getString("Message");
            if (status) {

                Toast.makeText(EditProfileActivity.this, context.getString(R.string.toast_update_succsess), Toast.LENGTH_LONG).show();
                this.finish();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
