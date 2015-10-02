package com.stxnext.intranet2.broadcast;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.stxnext.intranet2.IntranetApp;
import com.stxnext.intranet2.backend.model.workedHour.WorkedHours;
import com.stxnext.intranet2.backend.retrofit.WorkedHoursService;
import com.stxnext.intranet2.rest.IntranetRestAdapter;
import com.stxnext.intranet2.utils.Session;

import retrofit.RestAdapter;

/**
 * Created by bkosarzycki on 02.10.15.
 */
public class AlarmManagerService  extends IntentService {

    private Context mContext;
    private RestAdapter restAdapter;
    private WorkedHoursService workedHoursService;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public AlarmManagerService() {
        super(AlarmManagerService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mContext = IntranetApp.getContext();
        restAdapter = IntranetRestAdapter.build();
        workedHoursService = restAdapter.create(WorkedHoursService.class);

        String userId = Session.getInstance(mContext).getUserId();
        if (userId != null && !userId.isEmpty()) {
            try {
                final WorkedHours workedHours = workedHoursService.getUserWorkedHours(Integer.parseInt(userId));
                float diff =  workedHours.getToday().getDiff();

                if (diff == -8.0) {
                    PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
                    PowerManager.WakeLock wl = pm.newWakeLock(
                            PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
                    wl.acquire();
                    Toast.makeText(mContext, "Uzupełnij godziny w intranecie!", Toast.LENGTH_LONG).show();
                    wl.release();
                }
            } catch (Exception exc) {
                //There is no session with the server
                Log.w(AlarmManagerService.class.getName(), "Tried to download data - NO SESSION (cookies)");
            }
        }
    }
}
