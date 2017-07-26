package com.example.acer.notex;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TaskReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
       String task = intent.getStringExtra("TASK");
       int id= intent.getIntExtra("ID",0);
        Intent i=new Intent(context,NotificationActivity.class);
        i.putExtra("PENDING",intent.getStringExtra("TASK"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context,id,i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(context)
                .setContentTitle(intent.getStringExtra("TASK"))
                .setContentText("This task is pending click to view")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();

        nm.notify(100,notification);

    }
}
