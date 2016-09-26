package com.oozmakappa.oyeloans;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.oozmakappa.oyeloans.DataExtraction.AppController;
import com.oozmakappa.oyeloans.Models.LoanUser;
import com.oozmakappa.oyeloans.utils.SharedDataManager;
import com.squareup.picasso.Picasso;


public class MyProfilePage extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, Toolbar.OnMenuItemClickListener {


    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;

    @Override
    public void onStart() {
        super.onStart();
        Tracker t = ((AppController) this.getApplication()).getDefaultTracker();
        t.setScreenName("My Profile");
        t.send(new HitBuilders.ScreenViewBuilder().build());
        t.enableAutoActivityTracking(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.NavBarColor));
        }


        bindActivity();

        mAppBarLayout.addOnOffsetChangedListener(this);

        mToolbar.inflateMenu(R.menu.menu_main);
        mToolbar.setOnMenuItemClickListener(this);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

        Button saveButton = (Button) findViewById(R.id.profileProceedButton);
        if(!getIntent().getBooleanExtra("AllEdit", false))
            saveButton.setVisibility(View.GONE);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accountSummaryIntent = new Intent(MyProfilePage.this, ActivityHelperClass.class);
                startActivity(accountSummaryIntent);
            }
        });

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setElevation(0);
        }

        if(getIntent().getBooleanExtra("ShowInsufficientInformation", false))
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyProfilePage.this);
            alertDialogBuilder.setMessage("We could not get sufficient information from your Facebook profile. Please fill up all the fields in the next page");
            alertDialogBuilder.setTitle("Oops!!");

            alertDialogBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent editProfileIntent = new Intent(MyProfilePage.this, EditProfileActivity.class);
                    editProfileIntent.putExtra("AllEdit",true);
                    startActivity(editProfileIntent);
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

            setUpData();

        //Button editButton = (Button) findViewById(R.id.profil)
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void setUpData() {
        LoanUser user = SharedDataManager.getInstance().userObject;
        ((TextView) findViewById(R.id.profileName)).setText(user.fbUserName);
        ((TextView) findViewById(R.id.email)).setText(user.emailID);
        ((TextView) findViewById(R.id.main_textview_title)).setText(user.fbUserName);
        ((TextView) findViewById(R.id.DOBAgeValue)).setText(user.DOB);
        ((TextView) findViewById(R.id.phoneNumberValue)).setText((user.mobileNumber != null && user.mobileNumber.length() > 0) ? user.mobileNumber : "NA");
        String educationValue = user.highestEducation + " @ " + user.highestEducationPlace + " batch of " + user.highestEducationYear;
        ((TextView) findViewById(R.id.educationValue)).setText(educationValue);
        String workValue = "Current: " + user.workTitle + " @ " + user.workPlace;
        ((TextView) findViewById(R.id.employmentValue)).setText(workValue);
        String totalExp = "Total Work Experience: " + user.totalWorkExperience + " years";
        ((TextView) findViewById(R.id.totalExperience)).setText(totalExp);
        String addressValue = user.street + user.locaility + user.city + user.state;
        ((TextView) findViewById(R.id.addressValue)).setText(addressValue);
        ((TextView) findViewById(R.id.fbProfileLinkValue)).setText(user.fbProfileLink);

        if (user.fbProfilePicURL != null && user.fbProfilePicURL.length() > 0) {
            Picasso.with(this)
                    .load(user.fbProfilePicURL)
                    .placeholder(R.drawable.profile)
                    .resize(400, 400)
                    .into((ImageView) findViewById(R.id.userProfilePicture));
        }
    }

    private void bindActivity() {
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle = (TextView) findViewById(R.id.main_textview_title);
        //mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                //startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                // startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        Intent editProfileIntent = new Intent(MyProfilePage.this, EditProfileActivity.class);
        editProfileIntent.putExtra("AllEdit",getIntent().getBooleanExtra("AllEdit", false));
        startActivity(editProfileIntent);

        return false;
    }
}
