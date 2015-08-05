package com.stxnext.intranet2.broadcast;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.stxnext.intranet2.backend.model.impl.UserImpl;
import com.stxnext.intranet2.database.repo.UserRepository;
import com.stxnext.intranet2.utils.DBManager;

import java.security.spec.ECField;
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

                Log.i(TAG, "RINGING - number: " + incomingNumber);

                UserImpl foundEmployee = null;
                try {
                    DBManager dbManager = DBManager.getInstance(context);
                    List<UserImpl> employees = dbManager.getEmployees();
                    for (UserImpl user : employees) {
                        String userPhone = user.getPhoneNumber().replace(" ", "").replace("-","").replace("(","").replace(")","").replace(".","");
                        String incomingNumberComp = incomingNumber.replace(" ", "").replace("-", "").replace("(","").replace(")","").replace(".","");

                        if (userPhone.isEmpty() || userPhone.length() < 9
                            || incomingNumberComp.isEmpty() || incomingNumberComp.length() < 9)
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

                view = new LinearLayout(context);
                view.setGravity(Gravity.RIGHT);
                view.setPadding(15,15,15,15);
                view.setBackgroundColor(Color.WHITE);
                TextView textView = new TextView(context);
                textView.setText("STXNext: " + foundEmployee.getFirstName() + " " + foundEmployee.getLastName());
                if (android.os.Build.VERSION.SDK_INT > 16)
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                textView.setTextColor(Color.argb(240, 39, 147, 139));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                textView.setTypeface(null, Typeface.BOLD_ITALIC);

                WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                        PixelFormat.TRANSLUCENT);
                params.gravity = Gravity.RIGHT | Gravity.TOP;
                params.setTitle("Load Average");
                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                try {
                    view.addView(textView);
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
