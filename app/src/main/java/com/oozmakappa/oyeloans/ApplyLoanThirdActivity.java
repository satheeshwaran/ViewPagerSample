package com.oozmakappa.oyeloans;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.channguyen.rsv.RangeSliderView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.oozmakappa.oyeloans.Models.LoanSummaryModel;
import com.oozmakappa.oyeloans.Models.SuccessModel;
import com.oozmakappa.oyeloans.helper.WebServiceCallHelper;
import com.oozmakappa.oyeloans.utils.OyeConstants;
import com.oozmakappa.oyeloans.utils.SharedDataManager;
import com.oozmakappa.oyeloans.utils.Utils;


/**
 * Created by sankarnarayanan on 14/09/16.
 */
public class ApplyLoanThirdActivity extends AppCompatActivity {

    BroadcastReceiver receiver;
    IntentFilter filter;
    TextView secondsTextView;
    Boolean smsOTPReceived = false;
    Boolean smsOTPGenerated = false;
    String otp= "";

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

        Button verifyBtn = (Button) findViewById(R.id.profileProceedButtonOtp);
        //verifyBtn.setEnabled(false);

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ///Strictly for debugging without otp flow...
                //makeLoanApplicationCall();

                String smsOTP = ((TextView) findViewById(R.id.otpEntryField)).getText().toString();
                String mobileNumber = SharedDataManager.getInstance().userObject.mobileNumber;
                if (Utils.checkIfOnlyNumbers(smsOTP) && mobileNumber.length()>0) {
                    com.oozmakappa.oyeloans.utils.Utils.showLoading(ApplyLoanThirdActivity.this, "Validating your mobile number...");

                    WebServiceCallHelper webServiceHelper = new WebServiceCallHelper(new WebServiceCallHelper.OnWebServiceRequestCompletedListener() {
                        @Override
                        public void onRequestCompleted(SuccessModel model, String errorMessage) {
                            com.oozmakappa.oyeloans.utils.Utils.removeLoading();
                            if (errorMessage == null && model!=null && model.getStatus().equals("success")) {
                                makeLoanApplicationCall();
                            }else{
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ApplyLoanThirdActivity.this);
                                alertDialogBuilder.setTitle("Error!");
                                alertDialogBuilder.setMessage(model.getDescription());

                                alertDialogBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
                        }
                    });
                    webServiceHelper.validateOTPService(smsOTP,mobileNumber);
                }

            }
        });

        secondsTextView = (TextView) findViewById(R.id.secondsTextView);

        secondsTextView.setText("--");

        listenForSMSOTP();

        makeGenerateOTPCall();
    }

    private void makeGenerateOTPCall() {
        if (SharedDataManager.getInstance().userObject.mobileNumber != null && SharedDataManager.getInstance().userObject.mobileNumber.length() > 0) {
            com.oozmakappa.oyeloans.utils.Utils.showLoading(ApplyLoanThirdActivity.this, "Sending an OTP to your mobile number...");
            //TO-DO:// add loan object here...
            final WebServiceCallHelper webServiceHelper = new WebServiceCallHelper(new WebServiceCallHelper.OnWebServiceRequestCompletedListener() {
                @Override
                public void onRequestCompleted(SuccessModel model, String errorMessage) {
                    com.oozmakappa.oyeloans.utils.Utils.removeLoading();
                    if (model.getStatus().equals("success")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                smsOTPGenerated = true;
                                listenForSMSOTP();
                                setupCounter();
                            }
                        });

                    } else {
                        showOTPGenertionError();
                    }
                }
            });
            webServiceHelper.generateOTPService(SharedDataManager.getInstance().userObject.mobileNumber);
        }
    }

    private void showOTPGenertionError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ApplyLoanThirdActivity.this);
                alertDialogBuilder.setTitle("Something went wrong!");
                alertDialogBuilder.setMessage("Please try again later or enter a valid mobile number");

                alertDialogBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    private void listenForSMSOTP() {

        try {
            receiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();

                    if (action.equals("SMS OTP ACTION")) {
                        //OTP for your mobile number verification is 666443. Do not share this OTP with anyone for security reasons
                         String otpMessage = intent.getStringExtra("OTP");
                        otp = otpMessage.substring(otpMessage.substring(0,otpMessage.indexOf(".")).lastIndexOf(" "), otpMessage.indexOf("."));
                        ((TextView) findViewById(R.id.otpEntryField)).setText(otp);
                        smsOTPReceived = true;
                    }
                }

            };

            filter = new IntentFilter("SMS OTP ACTION");
            registerReceiver(receiver, filter);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private void setupCounter() {


        final CircularProgressBar circularProgressBar = (CircularProgressBar) findViewById(R.id.sms_otp_progress_bar);
        final int animationDuration = 1000; // 2500ms = 2,5s
        circularProgressBar.setProgressWithAnimation(100, animationDuration);

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                secondsTextView.setText(Integer.toString((int) (millisUntilFinished / 1000)));
                circularProgressBar.setProgressWithAnimation((int) ((millisUntilFinished / 1000) / 0.60), animationDuration);
            }

            public void onFinish() {
                secondsTextView.setText("0");
                circularProgressBar.setProgressWithAnimation(0, animationDuration);

                if (!smsOTPReceived && !((Activity) ApplyLoanThirdActivity.this).isFinishing()) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ApplyLoanThirdActivity.this);
                    alertDialogBuilder.setTitle("Something went wrong!");
                    alertDialogBuilder.setMessage("Please enter OTP manually");

                    alertDialogBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(((TextView) findViewById(R.id.otpEntryField)), InputMethodManager.SHOW_IMPLICIT);
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }

        }.start();

        /*final int[] i = {30};
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                i[0]--;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        secondsTextView.setText(Integer.toString(i[0]));
                    }
                });
            }
        }, 0, 1000);*/
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        public void onClick(View v) {
            onBackPressed();
        }
    };

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else if (getFragmentManager().getBackStackEntryCount() == 1) {
            moveTaskToBack(false);
        } else {
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

    private void makeLoanApplicationCall(){

        SharedDataManager.getInstance().activeApplication.loanUserObject = SharedDataManager.getInstance().userObject;

        WebServiceCallHelper webServiceHelper = new WebServiceCallHelper(new WebServiceCallHelper.OnWebServiceRequestCompletedListener() {
            @Override
            public void onRequestCompleted(SuccessModel model, String errorMessage) {
                com.oozmakappa.oyeloans.utils.Utils.removeLoading();
                if (errorMessage == null && model != null && model.getStatus().equals("success")) {
                    makeEmploymenInfoCall();

                }else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ApplyLoanThirdActivity.this);
                    alertDialogBuilder.setTitle("Error!");
                    alertDialogBuilder.setMessage(model.getDescription());

                    alertDialogBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
        webServiceHelper.makeNewApplicationServiceCall(SharedDataManager.getInstance().activeApplication, com.oozmakappa.oyeloans.DataExtraction.Utils.getDeviceId(ApplyLoanThirdActivity.this));
    }

    private void makeEmploymenInfoCall(){

        WebServiceCallHelper webServiceHelper = new WebServiceCallHelper(new WebServiceCallHelper.OnWebServiceRequestCompletedListener() {
            @Override
            public void onRequestCompleted(SuccessModel model, String errorMessage) {
                com.oozmakappa.oyeloans.utils.Utils.removeLoading();
                if (errorMessage == null && model != null && model.getStatus().equals("success")) {
                    makeBankInfoService();

                }else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ApplyLoanThirdActivity.this);
                    alertDialogBuilder.setTitle("Error!");
                    if (model!=null)
                        alertDialogBuilder.setMessage(model.getDescription());
                    else
                        alertDialogBuilder.setMessage("Unkown error");
                    alertDialogBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
        webServiceHelper.makeEmploymentInfoServiceCall(SharedDataManager.getInstance().activeApplication);

    }

    private void makeBankInfoService(){
        WebServiceCallHelper webServiceHelper = new WebServiceCallHelper(new WebServiceCallHelper.OnWebServiceRequestCompletedListener() {
            @Override
            public void onRequestCompleted(SuccessModel model, String errorMessage) {
                com.oozmakappa.oyeloans.utils.Utils.removeLoading();
                if (errorMessage == null && model != null && model.getStatus().equals("success")) {
                    Utils.removeLoading();
                    Intent thanksScreen = new Intent(ApplyLoanThirdActivity.this, ApplicationCompletedActivity.class);
                    startActivity(thanksScreen);
                    ApplyLoanThirdActivity.this.finish();

                }else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ApplyLoanThirdActivity.this);
                    alertDialogBuilder.setTitle("Error!");
                    if (model!=null)
                    alertDialogBuilder.setMessage(model.getDescription());
                    else
                        alertDialogBuilder.setMessage("Unkown error");

                    alertDialogBuilder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
        webServiceHelper.makeBankInfoServiceCall(SharedDataManager.getInstance().activeApplication);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (receiver != null) {
                unregisterReceiver(receiver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (receiver != null) {
                unregisterReceiver(receiver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (smsOTPGenerated)
            listenForSMSOTP();
    }
}
