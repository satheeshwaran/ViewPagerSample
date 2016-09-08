package com.oozmakappa.oyeloans;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.oozmakappa.oyeloans.Adapters.LoanSummaryAdapter;
import com.oozmakappa.oyeloans.Models.LoanSummaryModel;

import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import com.oozmakappa.oyeloans.utils.FacebookHelperUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AccountSummaryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ResideMenu resideMenu;
    private AccountSummaryActivity mContext;
    ListView list;
    LoanSummaryAdapter adapter;
    public  AccountSummaryActivity CustomListView = null;
    public ArrayList<LoanSummaryModel> CustomListViewValuesArr = new ArrayList<LoanSummaryModel>();

    public String responseData = "{\"loan_status_history\": [{\"loan_id\":1, \"loan_status\":\"Closed\"},{\"loan_id\":3, \"loan_status\":\"Pre- Closed\"},{\"loan_id\":107, \"loan_status\":\"Closed\"}]}";


    /****** Function to set data in ArrayList *************/
    public void setListData(String arrayData)
    {
        try {
            JSONObject json = new JSONObject(responseData);
            JSONArray array = json.getJSONArray("loan_status_history");

            for (int i=0; i< array.length(); i++){
                JSONObject currentObj = array.getJSONObject(i);
                final LoanSummaryModel loanModel = new LoanSummaryModel();
                loanModel.setLoanStatus(currentObj.getString("loan_status"));
                loanModel.setLoanId(currentObj.getString("loan_id"));
                CustomListViewValuesArr.add( loanModel );
            }
        }catch (Exception e){

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_summary);
        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(this.getResources().getColor(R.color.NavBarColor));

        mContext = this;
        setUpMenu();
        setListData(responseData);
        Resources res =getResources();
        ListView listView= ( ListView )findViewById( R.id.listView );
        adapter = new LoanSummaryAdapter(this,  CustomListViewValuesArr, res);
        listView.setAdapter(adapter);
        //list.setAdapter(adapter);

        ImageView image = (ImageView) findViewById(R.id.menuIcon);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("Loan Summary");
//        toolbar.setBackgroundColor(Color.parseColor("#197EE6"));
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.account_summary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }


        return true;
    }


    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        //resideMenu.setUse3D(true);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };


}
