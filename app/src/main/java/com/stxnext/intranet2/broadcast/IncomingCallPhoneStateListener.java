package com.stxnext.intranet2.broadcast;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by bkosarzycki on 05.08.15.
 */
public class IncomingCallPhoneStateListener extends PhoneStateListener {

    private static final String TAG = "IncCallPhoneStListener";

    public IncomingCallPhoneStateListener() {
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {

        switch(state){
            case TelephonyManager.CALL_STATE_RINGING:
                Log.i(TAG, "RINGING - show number and name: " + incomingNumber);
                break;
            default:
                Log.i(TAG, "STOPPED - hide number and name");
                break;
        }
    }

}
