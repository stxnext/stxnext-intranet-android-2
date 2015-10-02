package com.stxnext.intranet2.broadcast;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.stxnext.intranet2.IntranetApp;
import com.stxnext.intranet2.R;
import com.stxnext.intranet2.activity.CommonProfileActivity;
import com.stxnext.intranet2.activity.MyProfileActivity;
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
    public static int ID_NOTIFICATION = 2018;

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
                String fillHoursString = getString(R.string.notif_register_hours_in_intranet);

                if (diff == -8.0) {
                    PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
                    PowerManager.WakeLock wl = pm.newWakeLock(
                            PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
                    wl.acquire();
                    Toast.makeText(mContext, fillHoursString, Toast.LENGTH_LONG).show();
                    wl.release();

                    Intent notificationIntent = new Intent(mContext, MyProfileActivity.class);
                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    PendingIntent pendingIntent =
                            PendingIntent.getActivity(mContext, 0, notificationIntent, 0);

                    Notification notification = new NotificationCompat.Builder(mContext)
                            .setContentTitle(fillHoursString)
                            .setContentText(fillHoursString)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setAutoCancel(true)
                            .setOngoing(true)
                            .setContentIntent(pendingIntent)
                            .setColor(Color.parseColor("#FF2196F3"))
                            .build();

                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(ID_NOTIFICATION, notification);
                }
            } catch (Exception exc) {
                //There is no session with the server
                Log.w(AlarmManagerService.class.getName(), "Tried to download data - NO SESSION (cookies)");
            }
        }
    }
}
