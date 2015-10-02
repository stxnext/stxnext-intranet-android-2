package com.stxnext.intranet2.broadcast;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.stxnext.intranet2.IntranetApp;

/**
 * Created by bkosarzycki on 02.10.15.
 */
public class AlarmManagerService  extends IntentService {

    private Context mContext;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public AlarmManagerService() {
        super(AlarmManagerService.class.getName());
    }


//    @Override
//    public void onReceive(Context context, Intent intent) {
////        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
////        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
////        //Acquire the lock
////        wl.acquire();
//        // show toast
//        //wl.release();
//
//        int aaa = 1;
//        aaa = 2;
//    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mContext = IntranetApp.getContext();
        int aaa = 1;
        aaa = 2;
    }
}
