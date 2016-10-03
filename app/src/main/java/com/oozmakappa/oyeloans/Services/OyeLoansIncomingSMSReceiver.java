package com.oozmakappa.oyeloans.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.oozmakappa.oyeloans.utils.OyeConstants;

/**
 * Created by satheeshwaran on 9/19/16.
 */
public class OyeLoansIncomingSMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    if (senderNum.equals(OyeConstants.smsOTPIncomingMessageSenderNumber1) || senderNum.equals(OyeConstants.smsOTPIncomingMessageSenderNumber2) || senderNum.contains(OyeConstants.smsOTPIncomingMessageSenderNumber3)) {
                        System.out.println(message);
                        try {
                            Intent sendOTPIntent = new Intent("SMS OTP RECIEVED");
                            sendOTPIntent.putExtra("OTP", message);
                            sendOTPIntent.setAction("SMS OTP ACTION");
                            context.sendBroadcast(sendOTPIntent);

                        } catch (Exception e) {
                            System.out.print(e);
                        }
                    }

                }
            }
        } catch (Exception e) {
        }
    }
}
