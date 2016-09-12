package com.oozmakappa.oyeloans;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReferFriendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referralpage);
        getSupportActionBar().setTitle("Refer a friend");
        Button sendInvite = (Button)findViewById(R.id.inviteFriendsButton);

        sendInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
                txtIntent .setType("text/plain");
                txtIntent .putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Code");
                txtIntent .putExtra(android.content.Intent.EXTRA_TEXT, "Share my code to friends");
                startActivity(Intent.createChooser(txtIntent ,"Share"));

            }

        });

    }



}

