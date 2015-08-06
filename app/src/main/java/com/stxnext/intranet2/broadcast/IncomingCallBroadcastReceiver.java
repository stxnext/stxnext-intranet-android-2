package com.stxnext.intranet2.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by bkosarzycki on 05.08.15.
 */
public class IncomingCallBroadcastReceiver  extends BroadcastReceiver {

    private static final String TAG = "IncCallBroadcastRcvr";
    private static TelephonyManager telephony;
    private static IncomingCallPhoneStateListener customPhoneListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        if (customPhoneListener == null) {
            customPhoneListener = new IncomingCallPhoneStateListener(context);
            telephony.listen(customPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        }

        //Bundle bundle = intent.getExtras();
        //String phoneNr = bundle.getString("incoming_number");
        //Log.i(TAG, "Incoming call: " + phoneNr);
    }

}
