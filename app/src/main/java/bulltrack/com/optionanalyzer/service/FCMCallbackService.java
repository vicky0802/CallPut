package bulltrack.com.optionanalyzer.service;

import android.util.Log;
import android.widget.RemoteViews;
import bulltrack.com.optionanalyzer.application.MyGreeksApplication;
import bulltrack.com.optionanalyzer.constants.Constants;
import bulltrack.com.optionanalyzer.dao.StrategyResultsFilter;
import bulltrack.com.optiongreeks13.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class FCMCallbackService extends FirebaseMessagingService {
    private static final String TAG = "FCMCallbackService";
    StrategyResultsFilter filter = null;

    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage != null) {
            try {
                if (remoteMessage.getData() != null) {
                    String str = remoteMessage.getData().get("alert_type");
                    String str2 = remoteMessage.getData().get("alert_title");
                    String str3 = remoteMessage.getData().get("alert_content");
                    String str4 = remoteMessage.getData().get("actionactivity");
                    if (str4 == null || str4.trim().equals("")) {
                        str4 = "bulltrack.com.optionanalyzer.activity.MainActivity";
                    }
                    String str5 = str4;
                    if (str.trim().equalsIgnoreCase(Constants.FCM_ALERT_TYPE_STRATEGY.trim())) {
                        StrategyResultsFilter strategyResultsFilter = new StrategyResultsFilter();
                        this.filter = strategyResultsFilter;
                        strategyResultsFilter.setSymbol(remoteMessage.getData().get("symbol").toString());
                        this.filter.setStrategyId(Integer.parseInt(remoteMessage.getData().get("strategyid")));
                        this.filter.setStrategySubKey(Integer.parseInt(remoteMessage.getData().get("strategysubid")));
                        this.filter.setExpectedGain(Float.parseFloat(remoteMessage.getData().get("expectedgain")));
                        this.filter.setInvestment(Float.parseFloat(remoteMessage.getData().get("investment")));
                        this.filter.setInterestCost(Float.parseFloat(remoteMessage.getData().get("interestcost")));
                        this.filter.setRank(Integer.parseInt(remoteMessage.getData().get("rank")));
                        this.filter.setRunSeq(Integer.parseInt(remoteMessage.getData().get("runseq")));
                        this.filter.setAudience(Integer.parseInt(remoteMessage.getData().get("audience")));
                        this.filter.setUpdD(new Date(Long.parseLong(remoteMessage.getData().get("upd"))));
                        this.filter.setNetDebit(Float.parseFloat(remoteMessage.getData().get("netdebit")));
                        this.filter.setMaxRisk(Float.parseFloat(remoteMessage.getData().get("maxrisk")));
                        this.filter.setMaxGain(Float.parseFloat(remoteMessage.getData().get("maxgain")));
                        this.filter.setBreakevenDown(Float.parseFloat(remoteMessage.getData().get("breakevendown")));
                        this.filter.setBreakevenUp(Float.parseFloat(remoteMessage.getData().get("breakevenup")));
                        this.filter.setStrategyName(remoteMessage.getData().get("strategyname"));
                        this.filter.setTrend_c(remoteMessage.getData().get("trendc"));
                        this.filter.setNoOflegs(Integer.parseInt(remoteMessage.getData().get("nooflegs")));
                        this.filter.setLotSize(Integer.parseInt(remoteMessage.getData().get("lotsize")));
                        sendNotificationStrategy(remoteMessage.getNotification(), this.filter, str5, str2, str3);
                        return;
                    }
                    Log.d(TAG, "Message Received");
                    sendNotificationGeneral(remoteMessage.getNotification(), this.filter, str5, str2, str3);
                }
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x00ee  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void sendNotificationStrategy(com.google.firebase.messaging.RemoteMessage.Notification r11, StrategyResultsFilter r12, String r13, String r14, String r15) {
        /*
            r10 = this;
            r11 = 2
            android.media.RingtoneManager.getDefaultUri(r11)
            if (r13 == 0) goto L_0x0020
            java.lang.String r11 = r13.trim()
            java.lang.String r0 = ""
            boolean r11 = r11.equals(r0)
            if (r11 != 0) goto L_0x0020
            android.content.Intent r11 = new android.content.Intent     // Catch:{ ClassNotFoundException -> 0x001c }
            java.lang.Class r13 = java.lang.Class.forName(r13)     // Catch:{ ClassNotFoundException -> 0x001c }
            r11.<init>(r10, r13)     // Catch:{ ClassNotFoundException -> 0x001c }
            goto L_0x0021
        L_0x001c:
            r11 = move-exception
            r11.printStackTrace()
        L_0x0020:
            r11 = 0
        L_0x0021:
            java.util.Date r13 = new java.util.Date
            r13.<init>()
            long r0 = r13.getTime()
            r2 = 1000(0x3e8, double:4.94E-321)
            long r0 = r0 / r2
            r2 = 2147483647(0x7fffffff, double:1.060997895E-314)
            long r0 = r0 % r2
            int r13 = (int) r0
            java.lang.String r0 = "result"
            r11.putExtra(r0, r12)
            java.lang.String r12 = java.lang.Integer.toString(r13)
            r11.setAction(r12)
            androidx.core.app.TaskStackBuilder r12 = androidx.core.app.TaskStackBuilder.create(r10)
            androidx.core.app.TaskStackBuilder r11 = r12.addNextIntentWithParentStack(r11)
            r12 = 0
            r0 = 134217728(0x8000000, float:3.85186E-34)
            android.app.PendingIntent r11 = r11.getPendingIntent(r12, r0)
            java.lang.String r12 = "notification"
            java.lang.Object r12 = r10.getSystemService(r12)
            android.app.NotificationManager r12 = (android.app.NotificationManager) r12
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 26
            r2 = 2131165388(0x7f0700cc, float:1.7944992E38)
            r3 = 2131623936(0x7f0e0000, float:1.8875038E38)
            java.lang.String r4 = "/"
            java.lang.String r5 = "android.resource://"
            r6 = 1
            if (r0 < r1) goto L_0x00ee
            java.lang.String r0 = "cpa_strategy"
            android.media.AudioAttributes$Builder r1 = new android.media.AudioAttributes$Builder
            r1.<init>()
            r7 = 5
            android.media.AudioAttributes$Builder r1 = r1.setUsage(r7)
            r7 = 4
            android.media.AudioAttributes$Builder r1 = r1.setContentType(r7)
            android.media.AudioAttributes r1 = r1.build()
            android.app.NotificationChannel r8 = new android.app.NotificationChannel
            java.lang.String r9 = "CPA Strategy"
            r8.<init>(r0, r9, r7)
            java.lang.String r7 = "Call Put Analyzer Strategies"
            r8.setDescription(r7)
            r8.enableLights(r6)
            r8.setShowBadge(r6)
            r8.enableVibration(r6)
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            r7.append(r5)
            java.lang.String r5 = r10.getPackageName()
            r7.append(r5)
            r7.append(r4)
            r7.append(r3)
            java.lang.String r3 = r7.toString()
            android.net.Uri r3 = android.net.Uri.parse(r3)
            r8.setSound(r3, r1)
            r12.createNotificationChannel(r8)
            android.app.Notification$Builder r1 = new android.app.Notification$Builder
            android.content.Context r3 = r10.getApplicationContext()
            r1.<init>(r3, r0)
            android.app.Notification$Builder r0 = r1.setSmallIcon(r2)
            android.content.res.Resources r1 = r10.getResources()
            r2 = 2131558403(0x7f0d0003, float:1.874212E38)
            android.graphics.Bitmap r1 = android.graphics.BitmapFactory.decodeResource(r1, r2)
            android.app.Notification$Builder r0 = r0.setLargeIcon(r1)
            android.app.Notification$Builder r14 = r0.setContentTitle(r14)
            android.app.Notification$Builder r14 = r14.setContentText(r15)
            android.app.Notification$Builder r14 = r14.setAutoCancel(r6)
            android.app.Notification$Builder r11 = r14.setContentIntent(r11)
            android.widget.RemoteViews r14 = r10.getComplexNotificationView()
            r11.setCustomBigContentView(r14)
            android.app.Notification r11 = r11.build()
            r12.notify(r13, r11)
            goto L_0x019e
        L_0x00ee:
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 16
            r7 = 6
            r8 = 2131558402(0x7f0d0002, float:1.8742119E38)
            if (r0 < r1) goto L_0x014f
            androidx.core.app.NotificationCompat$Builder r0 = new androidx.core.app.NotificationCompat$Builder
            r0.<init>(r10)
            androidx.core.app.NotificationCompat$Builder r0 = r0.setAutoCancel(r6)
            androidx.core.app.NotificationCompat$Builder r0 = r0.setSmallIcon(r2)
            android.content.res.Resources r1 = r10.getResources()
            android.graphics.Bitmap r1 = android.graphics.BitmapFactory.decodeResource(r1, r8)
            androidx.core.app.NotificationCompat$Builder r0 = r0.setLargeIcon(r1)
            androidx.core.app.NotificationCompat$Builder r14 = r0.setContentTitle(r14)
            androidx.core.app.NotificationCompat$Builder r14 = r14.setContentText(r15)
            androidx.core.app.NotificationCompat$Builder r11 = r14.setContentIntent(r11)
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            r14.append(r5)
            java.lang.String r15 = r10.getPackageName()
            r14.append(r15)
            r14.append(r4)
            r14.append(r3)
            java.lang.String r14 = r14.toString()
            android.net.Uri r14 = android.net.Uri.parse(r14)
            r11.setSound(r14)
            r11.setDefaults(r7)
            android.widget.RemoteViews r14 = r10.getComplexNotificationView()
            r11.setCustomBigContentView(r14)
            android.app.Notification r11 = r11.build()
            r12.notify(r13, r11)
            goto L_0x019e
        L_0x014f:
            androidx.core.app.NotificationCompat$Builder r0 = new androidx.core.app.NotificationCompat$Builder
            r0.<init>(r10)
            androidx.core.app.NotificationCompat$Builder r0 = r0.setAutoCancel(r6)
            androidx.core.app.NotificationCompat$Builder r0 = r0.setSmallIcon(r2)
            android.content.res.Resources r1 = r10.getResources()
            android.graphics.Bitmap r1 = android.graphics.BitmapFactory.decodeResource(r1, r8)
            androidx.core.app.NotificationCompat$Builder r0 = r0.setLargeIcon(r1)
            androidx.core.app.NotificationCompat$Builder r14 = r0.setContentTitle(r14)
            androidx.core.app.NotificationCompat$Builder r14 = r14.setContentText(r15)
            androidx.core.app.NotificationCompat$Builder r11 = r14.setContentIntent(r11)
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            r14.append(r5)
            java.lang.String r15 = r10.getPackageName()
            r14.append(r15)
            r14.append(r4)
            r14.append(r3)
            java.lang.String r14 = r14.toString()
            android.net.Uri r14 = android.net.Uri.parse(r14)
            r11.setSound(r14)
            r11.setDefaults(r7)
            android.app.Notification r11 = r11.build()
            r12.notify(r13, r11)
        L_0x019e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.service.FCMCallbackService.sendNotificationStrategy(com.google.firebase.messaging.RemoteMessage$Notification, bulltrack.com.optionanalyzer.dao.StrategyResultsFilter, java.lang.String, java.lang.String, java.lang.String):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x004f  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x00de  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void sendNotificationGeneral(com.google.firebase.messaging.RemoteMessage.Notification r16, StrategyResultsFilter r17, String r18, String r19, String r20) {
        /*
            r15 = this;
            r1 = r15
            r2 = r19
            r3 = r20
            r0 = 2
            android.media.RingtoneManager.getDefaultUri(r0)
            if (r18 == 0) goto L_0x0025
            java.lang.String r0 = r18.trim()
            java.lang.String r4 = ""
            boolean r0 = r0.equals(r4)
            if (r0 != 0) goto L_0x0025
            android.content.Intent r0 = new android.content.Intent     // Catch:{ ClassNotFoundException -> 0x0021 }
            java.lang.Class r4 = java.lang.Class.forName(r18)     // Catch:{ ClassNotFoundException -> 0x0021 }
            r0.<init>(r15, r4)     // Catch:{ ClassNotFoundException -> 0x0021 }
            goto L_0x0026
        L_0x0021:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0025:
            r0 = 0
        L_0x0026:
            androidx.core.app.TaskStackBuilder r4 = androidx.core.app.TaskStackBuilder.create(r15)
            androidx.core.app.TaskStackBuilder r0 = r4.addNextIntentWithParentStack(r0)
            r4 = 0
            r5 = 134217728(0x8000000, float:3.85186E-34)
            android.app.PendingIntent r0 = r0.getPendingIntent(r4, r5)
            java.lang.String r4 = "notification"
            java.lang.Object r4 = r15.getSystemService(r4)
            android.app.NotificationManager r4 = (android.app.NotificationManager) r4
            int r5 = android.os.Build.VERSION.SDK_INT
            r6 = 26
            r10 = 2131623936(0x7f0e0000, float:1.8875038E38)
            java.lang.String r11 = "/"
            java.lang.String r12 = "android.resource://"
            r13 = 2131165282(0x7f070062, float:1.7944777E38)
            java.lang.String r14 = "cpa_alert"
            r7 = 1
            if (r5 < r6) goto L_0x00de
            android.media.AudioAttributes$Builder r5 = new android.media.AudioAttributes$Builder
            r5.<init>()
            r6 = 5
            android.media.AudioAttributes$Builder r5 = r5.setUsage(r6)
            r6 = 4
            android.media.AudioAttributes$Builder r5 = r5.setContentType(r6)
            android.media.AudioAttributes r5 = r5.build()
            android.app.NotificationChannel r8 = new android.app.NotificationChannel
            java.lang.String r9 = "CPA Alerts"
            r8.<init>(r14, r9, r6)
            java.lang.String r6 = "Call Put Analyzer Trade Alerts"
            r8.setDescription(r6)
            r8.enableLights(r7)
            r8.setShowBadge(r7)
            r8.enableVibration(r7)
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            r6.append(r12)
            java.lang.String r9 = r15.getPackageName()
            r6.append(r9)
            r6.append(r11)
            r6.append(r10)
            java.lang.String r6 = r6.toString()
            android.net.Uri r6 = android.net.Uri.parse(r6)
            r8.setSound(r6, r5)
            r4.createNotificationChannel(r8)
            android.app.Notification$Builder r5 = new android.app.Notification$Builder
            android.content.Context r6 = r15.getApplicationContext()
            r5.<init>(r6, r14)
            android.app.Notification$Builder r5 = r5.setSmallIcon(r13)
            android.content.res.Resources r6 = r15.getResources()
            r8 = 2131558400(0x7f0d0000, float:1.8742115E38)
            android.graphics.Bitmap r6 = android.graphics.BitmapFactory.decodeResource(r6, r8)
            android.app.Notification$Builder r5 = r5.setLargeIcon(r6)
            android.app.Notification$Builder r2 = r5.setContentTitle(r2)
            android.app.Notification$Builder r2 = r2.setContentText(r3)
            android.app.Notification$Builder r2 = r2.setAutoCancel(r7)
            android.app.Notification$Builder r0 = r2.setContentIntent(r0)
            java.util.Date r2 = new java.util.Date
            r2.<init>()
            long r2 = r2.getTime()
            r5 = 1000(0x3e8, double:4.94E-321)
            long r2 = r2 / r5
            r5 = 2147483647(0x7fffffff, double:1.060997895E-314)
            long r2 = r2 % r5
            int r3 = (int) r2
            android.app.Notification r0 = r0.build()
            r4.notify(r3, r0)
            goto L_0x014a
        L_0x00de:
            androidx.core.app.NotificationCompat$Builder r5 = new androidx.core.app.NotificationCompat$Builder
            r5.<init>(r15, r14)
            androidx.core.app.NotificationCompat$Builder r5 = r5.setAutoCancel(r7)
            androidx.core.app.NotificationCompat$Builder r5 = r5.setSmallIcon(r13)
            android.content.res.Resources r6 = r15.getResources()
            r7 = 2131558402(0x7f0d0002, float:1.8742119E38)
            android.graphics.Bitmap r6 = android.graphics.BitmapFactory.decodeResource(r6, r7)
            androidx.core.app.NotificationCompat$Builder r5 = r5.setLargeIcon(r6)
            androidx.core.app.NotificationCompat$Builder r5 = r5.setContentTitle(r2)
            androidx.core.app.NotificationCompat$Builder r5 = r5.setContentText(r3)
            androidx.core.app.NotificationCompat$Builder r0 = r5.setContentIntent(r0)
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            r5.append(r12)
            java.lang.String r6 = r15.getPackageName()
            r5.append(r6)
            r5.append(r11)
            r5.append(r10)
            java.lang.String r5 = r5.toString()
            android.net.Uri r5 = android.net.Uri.parse(r5)
            r0.setSound(r5)
            r5 = 6
            r0.setDefaults(r5)
            androidx.core.app.NotificationCompat$Builder r0 = r0.setContentTitle(r2)
            androidx.core.app.NotificationCompat$Builder r0 = r0.setContentText(r3)
            java.util.Date r2 = new java.util.Date
            r2.<init>()
            long r2 = r2.getTime()
            r5 = 1000(0x3e8, double:4.94E-321)
            long r2 = r2 / r5
            r5 = 2147483647(0x7fffffff, double:1.060997895E-314)
            long r2 = r2 % r5
            int r3 = (int) r2
            android.app.Notification r0 = r0.build()
            r4.notify(r3, r0)
        L_0x014a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: bulltrack.com.optionanalyzer.service.FCMCallbackService.sendNotificationGeneral(com.google.firebase.messaging.RemoteMessage$Notification, bulltrack.com.optionanalyzer.dao.StrategyResultsFilter, java.lang.String, java.lang.String, java.lang.String):void");
    }

    private RemoteViews getComplexNotificationView() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.item_strategy_results);
        remoteViews.setTextViewText(R.id.tv_item_strategy_results_stock, this.filter.getSymbol());
        remoteViews.setTextViewText(R.id.tv_item_strategy_results_strategy, this.filter.getStrategyName());
        remoteViews.setTextViewText(R.id.tv_item_strategy_results_risk_val, "₹ " + getGreekApplication().round2Decimals1000((double) this.filter.getMaxRisk()));
        remoteViews.setTextViewText(R.id.tv_item_strategy_results_gain_val, "₹ " + getGreekApplication().round2Decimals1000((double) this.filter.getMaxGain()));
        remoteViews.setTextViewText(R.id.tv_item_strategy_results_legs_count, this.filter.getNoOflegs() + " Legs");
        remoteViews.setImageViewResource(R.id.img_item_strategy_results_payoff, getGreekApplication().getPayoffDiagram(this.filter.getStrategyId()));
        Calendar instance = Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta"));
        instance.set(11, 0);
        instance.set(12, 0);
        instance.set(13, 0);
        instance.set(14, 0);
        instance.getTimeInMillis();
        Calendar instance2 = Calendar.getInstance();
        instance2.setTime(this.filter.getUpdD());
        instance2.set(11, 0);
        instance2.set(12, 0);
        instance2.set(13, 0);
        instance2.set(14, 0);
        instance2.getTimeInMillis();
        return remoteViews;
    }

    /* access modifiers changed from: protected */
    public MyGreeksApplication getGreekApplication() {
        return (MyGreeksApplication) getApplicationContext();
    }
}
