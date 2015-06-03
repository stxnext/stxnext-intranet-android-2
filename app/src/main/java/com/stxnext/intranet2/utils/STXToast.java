package com.stxnext.intranet2.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.stxnext.intranet2.R;

/**
 * Created by Tomasz Konieczny on 2015-06-03.
 */
public class STXToast {

    public static void show(Context context, int resText) {
        Toast toast = Toast.makeText(context, resText, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.getView().setBackgroundResource(R.drawable.toast_background);
        TextView toastTextView = (TextView) toast.getView().findViewById(android.R.id.message);
        toastTextView.setTextColor(Color.WHITE);
        toast.show();
    }

}
