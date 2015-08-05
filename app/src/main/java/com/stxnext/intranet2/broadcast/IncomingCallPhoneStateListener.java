package com.stxnext.intranet2.broadcast;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by bkosarzycki on 05.08.15.
 */
public class IncomingCallPhoneStateListener extends PhoneStateListener {

    private static final String TAG = "IncCallPhoneStListener";
    private Context context;
    private TextView view;

    public IncomingCallPhoneStateListener(Context context) {
        this.context = context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {

        switch(state){
            case TelephonyManager.CALL_STATE_RINGING:
                Log.i(TAG, "RINGING - show number and name: " + incomingNumber);

                Toast.makeText(context, "onCreate", Toast.LENGTH_LONG).show();
                view = new TextView(context);
                view.setText("DZWONI: " + incomingNumber);
                view.setTextColor(Color.BLUE);
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

                WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                        PixelFormat.TRANSLUCENT);
                params.gravity = Gravity.RIGHT | Gravity.TOP;
                params.setTitle("Load Average");
                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                try {
                    wm.addView(view, params);
                } catch (Exception exc) {
                    Log.e(TAG, exc.toString());
                }

                break;
            default:
                Log.i(TAG, "STOPPED - hide number and name");
                if (view != null) {
                    WindowManager winMan = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                    winMan.removeViewImmediate(view);
                    view = null;
                }
                break;
        }
    }

}
