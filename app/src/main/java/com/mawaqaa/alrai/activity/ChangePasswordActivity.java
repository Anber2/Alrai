package com.mawaqaa.alrai.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
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

public class ChangePasswordActivity extends Activity {
    EditText currentPasswordETXT, newPasswordEXT, confirmNewPasswordETXT;
    Button changePasswordBTN;
    Context context;
    Animation animShke;
    ProgressDialog progressBar;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        context = getApplicationContext();
        intiView();
        userId = new PrefManage(ChangePasswordActivity.this).getUserDetails();

        changePasswordBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkRule()) {

                    progressBar = ProgressDialog.show(ChangePasswordActivity.this, "", "Please Wait ...", true, false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.putOpt("CurrentPassword", currentPasswordETXT.getText().toString().trim());
                                jsonObject.putOpt("NewPassword", newPasswordEXT.getText().toString().trim());
                                jsonObject.putOpt("SecurityKey", urlClass.SecurityKey);
                                jsonObject.putOpt("UserId", userId);
                                makeVolleyRequest(urlClass.changePassword, jsonObject);
                            } catch (Exception xx) {
                                xx.toString();
                            }
                        }
                    }).start();


                }
            }
        });


    }

    private void intiView() {

        animShke = AnimationUtils.loadAnimation(context, R.anim.shake);
        currentPasswordETXT = (EditText) findViewById(R.id.current_password_etxt);
        newPasswordEXT = (EditText) findViewById(R.id.new_password_etxt);
        confirmNewPasswordETXT = (EditText) findViewById(R.id.confirm_new_password_etxt);
        changePasswordBTN = (Button) findViewById(R.id.change_password_btn);
    }


    private boolean checkRule() {
        if (currentPasswordETXT.getText().toString().trim().equals("")) {
            Toast.makeText(context, context.getString(R.string.password_validation), Toast.LENGTH_SHORT).show();
            currentPasswordETXT.startAnimation(animShke);
            return false;
        }
        if (newPasswordEXT.getText().toString().trim().equals("")) {
            Toast.makeText(context, context.getString(R.string.password_validation), Toast.LENGTH_SHORT).show();
            newPasswordEXT.startAnimation(animShke);
            return false;
        }

        if (confirmNewPasswordETXT.getText().toString().trim().equals("")) {
            Toast.makeText(context, context.getString(R.string.confirm_pass_validation), Toast.LENGTH_SHORT).show();
            confirmNewPasswordETXT.startAnimation(animShke);
            return false;
        }
        return true;
    }


    private void makeVolleyRequest(String url, JSONObject jsonObject) {

        RequestQueue queue = Volley.newRequestQueue(ChangePasswordActivity.this);
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
                ChangePasswordActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChangePasswordActivity.this, context.getString(R.string.toast_error_message), Toast.LENGTH_LONG).show();

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
            String message = response.getString("Message");
            boolean status = response.getBoolean("Status");

            if (status) {
                Toast.makeText(context, context.getString(R.string.change_password_successfully), Toast.LENGTH_LONG).show();
                this.finish();
            } else {
                Toast.makeText(context, context.getString(R.string.change_password_failed), Toast.LENGTH_LONG).show();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
