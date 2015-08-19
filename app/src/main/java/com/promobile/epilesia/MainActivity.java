package com.promobile.epilesia;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private PendingIntent pendingIntent;
    private NotificationManager mNotifyManager;
    private Builder mBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startSensing();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopSensing();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            stopSensing();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /************************************ SERVIÇO ************************************/

    private void toggleService(){
        Context context = getApplicationContext();
        Intent intent = new Intent(context, SensorService.class);
        // Try to stop the service if it is already running
        intent.addCategory(SensorService.TAG);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), TIMER_MONITORAMENTO_HEURISTICA, pendingIntent);

        if(!stopService(intent)){
            startService(intent);
        }
    }

    public void startSensing(){
        showNotification();
        toggleService();
        //this.finish();
    }

    private void stopSensing(){
        Intent intent = new Intent(getApplicationContext(), SensorService.class);
        intent.addCategory(SensorService.TAG);
        stopService(intent);
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyManager.cancelAll();

        Context context = getApplicationContext();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    /******************************	BARRA DE NOTIFICAÇÃO ******************************/

    public void showNotification(){
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(getString(R.string.app_name))
                .setContentText("Serviço Iniciado")
                .setSmallIcon(R.mipmap.ic_launcher);
        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_ONE_SHOT
                );

        mBuilder.setOngoing(true);
        mBuilder.setContentIntent(resultPendingIntent);
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyManager.notify(ID, mBuilder.build());
    }
    int ID = 101;
    private final int TIMER_MONITORAMENTO_HEURISTICA = 30;
}
