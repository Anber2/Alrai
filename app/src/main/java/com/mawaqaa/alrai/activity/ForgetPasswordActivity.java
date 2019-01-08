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
import com.mawaqaa.alrai.other.urlClass;

import org.json.JSONObject;

public class ForgetPasswordActivity extends Activity {

    EditText emailETXT;
    Button submitForgetPassBTN;
    ProgressDialog progressBar;
    Context context;
    Animation animShke;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        context = getApplicationContext();
        initView();

        submitForgetPassBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkRule()) {

                    progressBar = ProgressDialog.show(ForgetPasswordActivity.this, "", "Please Wait ...", true, false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.putOpt("Email", emailETXT.getText().toString().trim());
                                jsonObject.putOpt("SecurityKey", urlClass.SecurityKey);
                                makeVolleyRequest(urlClass.resetPassword, jsonObject);
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
        emailETXT = (EditText) findViewById(R.id.forget_password_etxt);
        submitForgetPassBTN = (Button) findViewById(R.id.submit_forget_btn);

    }

    private boolean checkRule() {
        if (emailETXT.getText().toString().trim().equals("")) {
            Toast.makeText(context, context.getString(R.string.email_validation), Toast.LENGTH_SHORT).show();
            emailETXT.startAnimation(animShke);
            return false;
        }

        return true;
    }


    private void makeVolleyRequest(String url, JSONObject jsonObject) {

        RequestQueue queue = Volley.newRequestQueue(ForgetPasswordActivity.this);
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
                ForgetPasswordActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ForgetPasswordActivity.this, context.getString(R.string.toast_error_message), Toast.LENGTH_LONG).show();

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
                Toast.makeText(context, context.getString(R.string.toast_reset_pass_succsess), Toast.LENGTH_LONG).show();
                this.finish();
            } else {
                Toast.makeText(context, context.getString(R.string.toast_reset_pass_fail), Toast.LENGTH_LONG).show();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
