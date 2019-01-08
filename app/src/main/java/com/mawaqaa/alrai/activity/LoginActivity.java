package com.mawaqaa.alrai.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mawaqaa.alrai.R;
import com.mawaqaa.alrai.other.PrefManage;
import com.mawaqaa.alrai.other.urlClass;

import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.internal.CallbackManagerImpl.RequestCodeOffset.Login;

public class LoginActivity extends AppCompatActivity {

    TextView forgetPasswordTXT, createAccountTXT;
    Context context;
    Animation animShke;
    EditText nameEXT, passwordEXT;
    Button loginButton;
    ImageView fbLogin;
    ProgressDialog progressBar;
    LoginButton facebookLoginButton;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView titleText = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title);
        titleText.setText(R.string.title_activity_login);

        context = getApplicationContext();
        initView();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkRule()) {

                    progressBar = ProgressDialog.show(LoginActivity.this, "", "Please Wait ...", true, false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.putOpt("UserEmail", nameEXT.getText().toString().trim());
                                jsonObject.putOpt("Password", passwordEXT.getText().toString().trim());
                                jsonObject.putOpt("SecurityKey", urlClass.SecurityKey);
                                makeVolleyRequest(urlClass.signIN, jsonObject);
                            } catch (Exception xx) {
                                xx.toString();
                            }
                        }
                    }).start();

//                    Intent intent= new Intent(LoginActivity.this,SettingActivity.class);
//                    startActivity(intent);

                }


            }
        });

        facebookLoginButton.setReadPermissions("public_profile email");

        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                if(AccessToken.getCurrentAccessToken() != null){
                    RequestData();

                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {
            }
        });





    }

    public void RequestData(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object,GraphResponse response) {

                JSONObject json = response.getJSONObject();
                try {
                    if(json != null){
                        String text = "<b>Name :</b> "+json.getString("name")+"<br><br><b>Email :</b> "+
                                json.getString("email")+"<br><br><b>Profile link :</b> "+json.getString("link");
                        Toast.makeText(LoginActivity.this,text,Toast.LENGTH_LONG).show();
//                        details_txt.setText(Html.fromHtml(text));
//                        profile.setProfileId(json.getString("id"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }



    private void initView() {
        animShke = AnimationUtils.loadAnimation(context, R.anim.shake);
        forgetPasswordTXT = (TextView) findViewById(R.id.login_forget_password_txt);
        createAccountTXT = (TextView) findViewById(R.id.login_create_account_txt);
        nameEXT = (EditText) findViewById(R.id.login_name_etxt);
        passwordEXT = (EditText) findViewById(R.id.login_password_etxt);
        loginButton = (Button) findViewById(R.id.login_btn);

        facebookLoginButton= (LoginButton) findViewById(R.id.fb_login_button);

        forgetPasswordTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);

            }
        });

        createAccountTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });
    }


    private boolean checkRule() {
        if (nameEXT.getText().toString().trim().equals("")) {
            Toast.makeText(context, context.getString(R.string.name_validation), Toast.LENGTH_SHORT).show();
            nameEXT.startAnimation(animShke);
            return false;
        }
        if (passwordEXT.getText().toString().trim().equals("")) {
            Toast.makeText(context, context.getString(R.string.password_validation), Toast.LENGTH_SHORT).show();
            passwordEXT.startAnimation(animShke);
            return false;
        }
        return true;
    }

    private void makeVolleyRequest(String url, JSONObject jsonObject) {

        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
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
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this,context.getString(R.string.toast_error_message), Toast.LENGTH_LONG).show();

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
            boolean SignInStatus = response.getBoolean("SignInStatus");
            String UserId = response.getString("UserId");
            String UserName = response.getString("UserName");

            if (SignInStatus) {
                Toast.makeText(LoginActivity.this, context.getString(R.string.toast_login_sucss), Toast.LENGTH_LONG).show();
                new PrefManage(LoginActivity.this).saveUserDetails(UserName, UserId);
                Intent intent = new Intent(LoginActivity.this, SettingActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, context.getString(R.string.toast_login_fail), Toast.LENGTH_LONG).show();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
