package com.mawaqaa.alrai.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText nameETXT, emailETXT, passwordETXT, confirmPasswordEXTX;
    Button submitBTN;
    Context context;
    Animation animShke;
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView titleText = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title);
        titleText.setText(R.string.title_activity_register);

        context = getApplicationContext();
        initView();

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkRule()) {

                    progressBar = ProgressDialog.show(RegisterActivity.this, "", "Please Wait ...", true, false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.putOpt("Email", emailETXT.getText().toString().trim());
                                jsonObject.putOpt("Password", passwordETXT.getText().toString().trim());
                                jsonObject.putOpt("UserName", nameETXT.getText().toString().trim());
                                jsonObject.putOpt("SecurityKey", urlClass.SecurityKey);
                                makeVolleyRequest(urlClass.signUpWithEmail, jsonObject);
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
        nameETXT = (EditText) findViewById(R.id.register_name_etxt);
        emailETXT = (EditText) findViewById(R.id.register_email_etxt);
        passwordETXT = (EditText) findViewById(R.id.register_password_etxt);
        confirmPasswordEXTX = (EditText) findViewById(R.id.register_confirm_password_etxt);
        submitBTN = (Button) findViewById(R.id.register_submit_btn);


    }


    private boolean checkRule() {
        if (nameETXT.getText().toString().trim().equals("")) {
            Toast.makeText(context, context.getString(R.string.name_validation), Toast.LENGTH_SHORT).show();
            nameETXT.startAnimation(animShke);
            return false;
        }
        if (emailETXT.getText().toString().trim().equals("")) {
            Toast.makeText(context, context.getString(R.string.email_validation), Toast.LENGTH_SHORT).show();
            emailETXT.startAnimation(animShke);
            return false;
        }

        if (passwordETXT.getText().toString().trim().equals("")) {
            Toast.makeText(context, context.getString(R.string.password_validation), Toast.LENGTH_SHORT).show();
            passwordETXT.startAnimation(animShke);
            return false;
        }

        if (confirmPasswordEXTX.getText().toString().trim().equals("")) {
            Toast.makeText(context, context.getString(R.string.confirm_pass_validation), Toast.LENGTH_SHORT).show();
            confirmPasswordEXTX.startAnimation(animShke);
            return false;
        }
        if(!(passwordETXT.getText().toString().trim().equals(confirmPasswordEXTX.getText().toString().trim())) ){
            Toast.makeText(context, context.getString(R.string.password_validation), Toast.LENGTH_SHORT).show();
            confirmPasswordEXTX .startAnimation(animShke);
            return false;
        }

        if(!(isEmailValid(emailETXT.getText().toString().trim()))){
            Toast.makeText(context, context.getString(R.string.email_validation), Toast.LENGTH_SHORT).show();
            emailETXT.startAnimation(animShke);
            return false;
        }

        return true;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void makeVolleyRequest(String url, JSONObject jsonObject) {

        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
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
                RegisterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this, context.getString(R.string.toast_error_message), Toast.LENGTH_LONG).show();

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
            boolean status = response.getBoolean("Status");
            String Message = response.getString("Message");
            String userId = response.getString("UserId");
            String userName = response.getString("UserName");
            new PrefManage(RegisterActivity.this).saveUserDetails(userName, userId);
            if (status) {
                Toast.makeText(RegisterActivity.this, context.getString(R.string.toast_register_sucss), Toast.LENGTH_LONG).show();

                this.finish();
            } else {
                Toast.makeText(RegisterActivity.this, context.getString(R.string.toast_register_fail), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
