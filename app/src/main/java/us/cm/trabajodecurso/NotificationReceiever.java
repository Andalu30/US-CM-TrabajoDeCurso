package us.cm.trabajodecurso;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import static android.support.constraint.Constraints.TAG;

public class NotificationReceiever extends BroadcastReceiver {


    @Override public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "onReceive: del NotificationReceiver! mandando notificacion!");


        String id_canal_notificaciones_solo_para_o = "esta llegua no es mi vieja llegua gris";

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelId = "nadim_notifs";
            id_canal_notificaciones_solo_para_o = channelId;

            CharSequence channelName = "Canal de notificaciones de NADIM reservas";
            int importance = NotificationManager.IMPORTANCE_MAX;
            NotificationChannel notificationChannel = null;
            notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,id_canal_notificaciones_solo_para_o)
                .setSmallIcon(R.drawable.ic_notifications)
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentTitle("Notificacion de prueba")
                .setContentText("Este es el contenido de la notificación.")
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        Log.i(TAG, "onReceive: SE VA A LLAMAR A LA NOTIFICACION!");

        if (intent.getAction().equals("MY_NOTIFICATION_MESSAGE")) {
            notificationManager.notify(0, builder.build());
        }
    }
}

