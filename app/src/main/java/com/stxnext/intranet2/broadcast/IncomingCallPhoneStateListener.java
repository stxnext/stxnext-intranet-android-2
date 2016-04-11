package com.stxnext.intranet2.broadcast;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
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
    private static LinearLayout view;

    private int yDelta;

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
                    int numberOfProcessors = Runtime.getRuntime().availableProcessors();
                    if (numberOfProcessors > 1) {
                        fadeIn(params);
                    } else {
                        wm.addView(view, params);
                    }
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

    private void fadeIn(final WindowManager.LayoutParams params) {
        final long startTime = System.currentTimeMillis();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                fadeInHandler(params, startTime);
            }
        }, 25);
    }

    /*This will handle the entire animation*/
    public void fadeInHandler(final WindowManager.LayoutParams params, final long startTime){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
            return;
        }
        int animationDurationMillis = 750;
        long timeNow = System.currentTimeMillis();
        float alpha = (timeNow - startTime)*1.0f/animationDurationMillis;
        if (alpha > 1.0f){
            alpha = 1.0f;
        }
        params.alpha = alpha;
        //Log.d(TAG, "ALPHA: " + params.alpha);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        try {
            windowManager.updateViewLayout(view, params);
        } catch (IllegalArgumentException illArgExc) {
            windowManager.addView(view, params);
        }

        if (timeNow-startTime < animationDurationMillis){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                public void run(){
                    fadeInHandler(params, startTime);
                }
            }, 25);
        }
    }

    @NonNull private WindowManager.LayoutParams createStxFrameViewParams() {
        WindowManager.LayoutParams params = null;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //ANDROID 5.0 and up
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    PixelFormat.TRANSLUCENT);
            params.verticalMargin = 0.3f;
        } else { //ANDROID 4.x
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE, //previosly: TYPE_SYSTEM_OVERLAY  //http://stackoverflow.com/questions/9656185/type-system-overlay-in-ics
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    PixelFormat.TRANSLUCENT);
        }
        params.gravity = Gravity.LEFT | Gravity.TOP;

        return params;
    }

    @NonNull private LinearLayout createStxFrameView(User foundEmployee) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.notification_caller_layout, null);
        TextView tv = (TextView)ll.findViewById(R.id.notification_caller_layout_caller_tv);
        tv.setText("STXNext: \n" + foundEmployee.getFirstName() + " " + foundEmployee.getLastName());
        if (android.os.Build.VERSION.SDK_INT > 16)
            tv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

        ImageView imageView = (ImageView)ll.findViewById(R.id.notification_caller_layout_profile_iv);
        Picasso.with(context)
                .load("https://intranet.stxnext.pl" + foundEmployee.getPhoto())
                .placeholder(R.drawable.avatar_placeholder)
                .resizeDimen(R.dimen.notif_caller_profile_pict_size, R.dimen.notif_caller_profile_pict_size)
                .centerCrop()
                .into(imageView);

        ll.findViewById(R.id.caller_layout_close_ll).setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        WindowManager winMan = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                        try {
                            winMan.removeViewImmediate(view);
                        } catch (Exception exc) {
                            Log.e(TAG, "CALLER WINDOW WAS NOT ATTACHED TO Window Manager.");
                        }

                        try {
                            winMan.removeViewImmediate((View) v.getParent().getParent());
                        } catch (Exception exc) {
                            Log.e(TAG, "CALLER VIEW PARENT WAS NOT ATTACHED TO Window Manager.");
                        }

                        return true;
                    }
                }
        );

        ll.findViewById(R.id.caller_layout_image_name_combined_ll).setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        final int X = (int) event.getRawX();
                        final int Y = (int) event.getRawY();
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_DOWN:
                                WindowManager.LayoutParams lParams = ((WindowManager.LayoutParams) ((LinearLayout) v.getParent()).getLayoutParams());
                                yDelta = Y;
                                break;
                            case MotionEvent.ACTION_MOVE:
                                break;
                            case MotionEvent.ACTION_POINTER_DOWN:
                                break;
                            case MotionEvent.ACTION_POINTER_UP:
                                break;
                            case MotionEvent.ACTION_UP:
                                LinearLayout layout = ((LinearLayout) v.getParent());
                                WindowManager.LayoutParams lPar = ((WindowManager.LayoutParams) layout.getLayoutParams());

                                WindowManager winMan = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                                Display display = winMan.getDefaultDisplay();
                                int height = display.getHeight();

                                int yDiff = yDelta - Y;
                                Log.d(TAG, "yDiff: " + yDiff);

                                if (yDiff > 0) {
                                    lPar.verticalMargin -= Math.abs(yDiff) * 1.0f / height;
                                } else {
                                    lPar.verticalMargin += Math.abs(yDiff) * 1.0f / height;
                                }

                                try {
                                    winMan.removeViewImmediate(layout);
                                } catch (Exception exc) {
                                    Log.e(TAG, "CALLER VIEW PARENT WAS NOT ATTACHED TO Window Manager.");
                                }
                                winMan.addView(layout, lPar);

                                break;
                        }
                        ll.invalidate();
                        return true;
                    }
                }
        );


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
