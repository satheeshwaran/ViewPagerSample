package com.oozmakappa.oyeloans.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.oozmakappa.oyeloans.ApplyLoanSecondActivity;
import com.oozmakappa.oyeloans.ApplyLoanThirdActivity;
import com.oozmakappa.oyeloans.DataExtraction.AppController;
import com.oozmakappa.oyeloans.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by sankarnarayanan on 14/09/16.
 */
public class ApplyLoanUploadDocuments extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_uploaddoc_details, null);
        return v;
    }


    @Override
    public void onStart() {
        CardView panCardView = (CardView) getActivity().findViewById(R.id.panCardView);
        panCardView.setOnClickListener(panCardListener);
        CardView salaryslipView = (CardView) getActivity().findViewById(R.id.salarySlipView);
        salaryslipView.setOnClickListener(salarySlipListener);
        ImageView slip1 = (ImageView)getActivity().findViewById(R.id.salaryslip_1);
        ImageView slip2 = (ImageView)getActivity().findViewById(R.id.salaryslip_2);
        ImageView slip3 = (ImageView)getActivity().findViewById(R.id.salaryslip_3);
        slip1.setOnClickListener(pdfListener);
        slip2.setOnClickListener(pdfListener);
        slip3.setOnClickListener(pdfListener);

        Button proceedButton = (Button) getActivity().findViewById(R.id.proceedAppFlowButtonuploadComplete);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent applicationPageThree = new Intent(getActivity(),ApplyLoanThirdActivity.class);
                startActivity(applicationPageThree);
            }
        });

        super.onStart();
        Tracker t = ((AppController) getActivity().getApplication()).getDefaultTracker();
        t.setScreenName("Loan application - upload document screen");
        t.send(new HitBuilders.ScreenViewBuilder().build());
        t.enableAutoActivityTracking(true);
    }


    public void selectPdfDocument(int identifier){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
    }

    View.OnClickListener salarySlipListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            CardView childView = (CardView) getActivity().findViewById(R.id.salarySlipChildView);

            if (childView.getVisibility() == View.GONE) {
                childView.setVisibility(View.VISIBLE);
            }else{
                childView.setVisibility(View.GONE);
            }
        }
    };


    View.OnClickListener pdfListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.salaryslip_1:
                    selectPdfDocument(3);
                    break;
                case R.id.salaryslip_2:
                    selectPdfDocument(4);
                    break;
                case R.id.salaryslip_3:
                    selectPdfDocument(5);
                    break;
                default:
                    break;
            }
        }
    };


    View.OnClickListener panCardListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
        }
    };


    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                if (data != null)
                {
                    try
                    {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                        //Make web service all here.
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)
            {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }



    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

