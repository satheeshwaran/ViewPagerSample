package com.oozmakappa.oyeloans;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;


/**
 * Created by sankarnarayanan on 09/09/16.
 */
public class ApplyLoanFirstActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applyloan_first_activity);
        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(this.getResources().getColor(R.color.NavigationMenuColor));

        ImageView backButton = (ImageView) findViewById(R.id.menuIcon);
        backButton.setOnClickListener(clickListener);

        SeekBar amountSeekbar = (SeekBar) findViewById(R.id.seekBar);
        final EditText amountEdit = (EditText) findViewById(R.id.editText);
        amountSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub

                int value = (progress * 1000) + 10000;
                amountEdit.setText(Integer.toString(value));
                amountEdit.clearFocus();

            }
        });





    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        public void onClick(View v) {
            onBackPressed();
        }
    };



}
