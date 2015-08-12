package com.stxnext.intranet2.broadcast;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.utils.DBManager;
import java.util.List;

/**
 * Created by bkosarzycki on 05.08.15.
 */
public class IncomingCallPhoneStateListener extends PhoneStateListener {

    private static final String TAG = "IncCallPhoneStListener";
    private Context context;
    private LinearLayout view;

    public IncomingCallPhoneStateListener(Context context) {
        this.context = context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {

        switch(state){
            case TelephonyManager.CALL_STATE_RINGING:

                Log.d(TAG, "RINGING - number: " + incomingNumber);

                User foundEmployee = null;
                try {
                    DBManager dbManager = DBManager.getInstance(context);
                    List<User> employees = dbManager.getEmployees();
                    for (User user : employees) {
                        String userPhone = replaceIllegalPhoneChars(user != null ? user.getPhoneNumber() : "");
                        String incomingNumberComp = replaceIllegalPhoneChars(incomingNumber != null ? incomingNumber : "");

                        if (!isPhoneNumberLengthCorrect(userPhone) || !isPhoneNumberLengthCorrect(incomingNumberComp) )
                            continue;

                        if (incomingNumberComp.substring(incomingNumberComp.length() - 9, incomingNumberComp.length())
                                .equals(userPhone.substring(userPhone.length() - 9, userPhone.length())))
                            foundEmployee = user;
                    }
                } catch (Exception exc) {
                    Log.e(TAG, "Error in getting caller data: " + exc.toString());
                }

                if (foundEmployee == null)
                    return;

                view = createStxFrameView(foundEmployee);
                WindowManager.LayoutParams params = createStxFrameViewParams();
                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                try {
                    wm.addView(view, params);
                } catch (Exception exc) {
                    Log.e(TAG, exc.toString());
                }

                break;
            default:
                Log.d(TAG, "STOPPED - hide number and name");
                if (view != null) {
                    try {
                        WindowManager winMan = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                        winMan.removeViewImmediate(view);
                    } catch (Exception exc) {
                        Log.e(TAG, "CALLER WINDOW WAS NOT ATTACHED TO Window Manager.");
                    }
                    view = null;
                }
                break;
        }
    }

    @NonNull private WindowManager.LayoutParams createStxFrameViewParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.RIGHT | Gravity.TOP;
        return params;
    }

    @NonNull private LinearLayout createStxFrameView(User foundEmployee) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.notification_caller_layout, null);
        TextView tv = (TextView)ll.findViewById(R.id.notification_caller_layout_caller_tv);
        tv.setText("STXNext: " + foundEmployee.getFirstName() + " " + foundEmployee.getLastName());
        if (android.os.Build.VERSION.SDK_INT > 16)
            tv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

        return ll;
    }

    private String replaceIllegalPhoneChars(String val) {
        if (val == null)
            return "";
        return val.replace(" ", "").replace("-","").replace("(","").replace(")","").replace(".","");
    }

    private boolean isPhoneNumberLengthCorrect(String userPhone) {
        return userPhone != null && !userPhone.isEmpty() && userPhone.length() >= 9;
    }

}
