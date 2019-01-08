package com.mawaqaa.alrai.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.mawaqaa.alrai.R;
import com.mawaqaa.alrai.other.PrefManage;

public class SettingActivity extends AppCompatActivity {

    TextView editProfileTXT, showProfileTXT, changePasswordTXT, favoriteMenuTXT, importantNewsTXT, logoutTXT;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView titleText = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.action_bar_title);
        titleText.setText(R.string.title_activity_setting);

        initView();
        userId = new PrefManage(SettingActivity.this).getUserDetails();

        editProfileTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        showProfileTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ViewProfileActivity.class);
                startActivity(intent);
            }
        });

        changePasswordTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        logoutTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingActivity.this);
                alertDialog.setTitle(getApplicationContext().getString(R.string.setting_logout));
                alertDialog.setMessage(getApplicationContext().getString(R.string.setting_logout_text));

                alertDialog.setNegativeButton(getApplicationContext().getString(R.string.setting_logout_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog.setPositiveButton(getApplicationContext().getString(R.string.setting_logout_yes)
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new PrefManage(SettingActivity.this).logOut();
                                Intent i = getBaseContext().getPackageManager()
                                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                startActivity(i);


                            }
                        });


                alertDialog.show();


            }
        });


    }

    private void initView() {
        editProfileTXT = (TextView) findViewById(R.id.edit_profile_TXT);
        showProfileTXT = (TextView) findViewById(R.id.show_profile_TXT);
        changePasswordTXT = (TextView) findViewById(R.id.change_password_TXT);
        favoriteMenuTXT = (TextView) findViewById(R.id.favorite_menu_TXT);
        importantNewsTXT = (TextView) findViewById(R.id.important_news_TXT);
        logoutTXT = (TextView) findViewById(R.id.logout_TXT);

    }


}
