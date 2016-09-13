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

import com.github.channguyen.rsv.RangeSliderView;
import com.oozmakappa.oyeloans.Adapters.EnterDetailsPagerAdapter;
import com.oozmakappa.oyeloans.Adapters.UploadDocsPagerAdapter;

/**
 * Created by sankarnarayanan on 14/09/16.
 */
public class ApplyLoanSecondActivity extends AppCompatActivity {

    ViewPager viewPager;

    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.applyloan_first_activity);
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

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new UploadDocsPagerAdapter(getSupportFragmentManager(), this));
        viewPager.addOnPageChangeListener(pageChangeListener);

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Bank Info"));
        tabLayout.addTab(tabLayout.newTab().setText("Upload Documents"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(tabSelectedListener);

        RangeSliderView sliderView = (RangeSliderView) findViewById(R.id.rsv_custom);
        sliderView.setEnabled(true);
        sliderView.setInitialIndex(1);
        sliderView.setClickable(false);

        sliderView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }

    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener(){
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };


    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            TabLayout.Tab tab = tabLayout.getTabAt(position);
            tab.select();

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


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

}
