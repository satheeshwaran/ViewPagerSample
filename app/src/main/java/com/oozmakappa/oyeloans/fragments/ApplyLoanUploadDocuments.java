package com.oozmakappa.oyeloans.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.oozmakappa.oyeloans.ApplyLoanThirdActivity;
import com.oozmakappa.oyeloans.DataExtraction.AppController;
import com.oozmakappa.oyeloans.R;
import com.oozmakappa.oyeloans.constants.Jsonconstants;
import com.oozmakappa.oyeloans.helper.MultiPartRequest;
import com.oozmakappa.oyeloans.utils.SharedDataManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by sankarnarayanan on 14/09/16.
 */
public class ApplyLoanUploadDocuments extends Fragment {


    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";
    private final String boundary = "apiclient-" + System.currentTimeMillis();
    private final String mimeType = "multipart/form-data;boundary=" + boundary;
    private byte[] multipartBody;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(bos);


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
        try {
            if (SharedDataManager.getInstance().activeApplication != null && SharedDataManager.getInstance().activeApplication.applicationID != null) {
                buildPart(dos, SharedDataManager.getInstance().activeApplication.applicationID.getBytes(), "app_id");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void selectPdfDocument(int identifier){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),identifier);
    }

    View.OnClickListener salarySlipListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            CardView childView = (CardView) getActivity().findViewById(R.id.salarySlipChildView);
            selectPdfDocument(2);
//            if (childView.getVisibility() == View.GONE) {
//                childView.setVisibility(View.VISIBLE);
//            }else{
//                childView.setVisibility(View.GONE);
//            }
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
                        buildPart(dos, getDatafromUri(data), "pan_image");
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            else if (resultCode == Activity.RESULT_CANCELED)
            {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == 2) {
            if (resultCode == Activity.RESULT_OK)
            {
                if (data != null)
                {
                    try
                    {
                        buildPart(dos, getDatafromUri(data), "stmt_image");
                        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                        multipartBody = bos.toByteArray();
                        uploadImageToServer();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            else if (resultCode == Activity.RESULT_CANCELED)
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
        //Clear memory.
        multipartBody = null;
        bos = null;
        dos = null;

        super.onDestroy();
    }

    private byte[] getDatafromUri(Intent data){
        Uri dataUri = data.getData();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis;
        try {
            fis = new FileInputStream(new File(dataUri.getPath()));
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] image = baos.toByteArray();
        return image;
    }


    private void buildPart(DataOutputStream dataOutputStream, byte[] fileData, String fileName) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\""
                + fileName + "\"" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileData);
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        // read file and write it into form...
        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(lineEnd);
    }

    private void uploadImageToServer(){
        String url = Jsonconstants.OL_BASE_URL.concat(Jsonconstants.OL_UPLOAD_DOCUMENTS_LINK);
        MultiPartRequest multipartRequest = new MultiPartRequest(url, null, mimeType, multipartBody, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String responseString = response.data.toString();
                Toast.makeText(getActivity(), "Upload successfully!".concat(responseString), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Upload failed!\r\n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(multipartRequest);
    }


}

