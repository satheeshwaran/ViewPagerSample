package com.oozmakappa.oyeloans;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.channguyen.rsv.RangeSliderView;
import com.oozmakappa.oyeloans.Adapters.UploadDocsPagerAdapter;

/**
 * Created by sankarnarayanan on 14/09/16.
 */
public class ApplyLoanThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.applyloan_third_activity);
        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.NavigationMenuColor));
        }

        ImageView backButton = (ImageView) findViewById(R.id.menuIcon);
        backButton.setOnClickListener(clickListener);

        RangeSliderView sliderView = (RangeSliderView) findViewById(R.id.rsv_custom_otp);
        sliderView.setEnabled(true);
        sliderView.setInitialIndex(2);
        sliderView.setClickable(false);

        sliderView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        ImageView circleView = (ImageView) findViewById(R.id.collapseIcon);

        circleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideShowProgress(v);
            }
        });

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        public void onClick(View v) {
            onBackPressed();
        }
    };

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
        else if(getFragmentManager().getBackStackEntryCount() == 1) {
            moveTaskToBack(false);
        }
        else {
            getFragmentManager().popBackStack();
        }
    }


    public void hideShowProgress(View v) {
        RangeSliderView sliderView = (RangeSliderView) findViewById(R.id.rsv_custom_otp);
        TextView textView1 = (TextView) findViewById(R.id.textView5);
        TextView textView2 = (TextView) findViewById(R.id.textView6);
        TextView textView3 = (TextView) findViewById(R.id.textView7);
        TextView textView4 = (TextView) findViewById(R.id.textView8);

        if (sliderView.getVisibility() == View.GONE) {
            sliderView.setVisibility(View.VISIBLE);
            textView1.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            textView3.setVisibility(View.VISIBLE);
            textView4.setVisibility(View.VISIBLE);
            ImageView currentImage = (ImageView) v;
            currentImage.setImageResource(R.drawable.ic_keyboard_arrow_up_white_48dp);
        } else {
            sliderView.setVisibility(View.GONE);
            textView1.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
            textView3.setVisibility(View.GONE);
            textView4.setVisibility(View.GONE);
            ImageView currentImage = (ImageView) v;
            currentImage.setImageResource(R.drawable.ic_keyboard_arrow_down_white_48dp);
        }

    }
}
