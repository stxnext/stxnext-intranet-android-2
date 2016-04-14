package com.stxnext.intranet2.broadcast;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.stxnext.intranet2.R;
import com.stxnext.intranet2.activity.MyProfileActivity;
import com.stxnext.intranet2.backend.model.workedHour.WorkedHours;
import com.stxnext.intranet2.backend.retrofit.WorkedHoursService;
import com.stxnext.intranet2.rest.IntranetRestAdapter;
import com.stxnext.intranet2.utils.Session;

import java.util.Calendar;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by bkosarzycki on 02.10.15.
 */
public class AlarmManagerService  extends IntentService {

    public static int ID_NOTIFICATION = 2018;
    private Context mContext;

    public AlarmManagerService() {
        super(AlarmManagerService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mContext = getApplicationContext();
        String userId = Session.getInstance(mContext).getUserId();
        IntranetRestAdapter.build().create(WorkedHoursService.class)
                .getUserWorkedHours(Integer.parseInt(userId), new Callback<WorkedHours>() {
                    @Override
                    public void success(WorkedHours workedHours, Response response) {
                        if (!isWeekend()) {
                            float diff = workedHours.getToday().getDiff();
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
                                        .setContentIntent(pendingIntent)
                                        .setColor(ContextCompat.getColor(mContext, R.color.stxnext_green_dark))
                                        .build();

                                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.notify(ID_NOTIFICATION, notification);
                            }
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        switch (error.getKind()) {
                            case NETWORK: {
                                Log.w(AlarmManagerService.class.getName(), "Couldn't get time-and-attendance - NO NETWORK");
                            }
                            default: {
                                Log.e(AlarmManagerService.class.getName(), "Couldn't get time-and-attendance " + error.toString());
                            }
                        }
                    }
                });
    }

    private boolean isWeekend() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY);
    }
}
