package br.com.petscare.petscare.helper;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import br.com.petscare.petscare.R;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class Notificacoes {
    private Context c;
    private Calendar calendar;

    public Notificacoes(String descricao, Context context){
        this.c = context;
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 6);

        Intent intent = new Intent("ALARME_DISPARADO");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        NotificationManager notificationManager = (NotificationManager) c.getSystemService(NOTIFICATION_SERVICE);
        //PendingIntent pendingIntent = PendingIntent.getActivity(c.getApplicationContext(), 0, new Intent(c.getApplicationContext(), MainActivity.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(c.getApplicationContext());
        builder.setTicker("ticker texto");
        builder.setContentTitle("Pets Care");
        builder.setContentText(descricao);
        builder.setSmallIcon(R.drawable.notify);
        builder.setLargeIcon(BitmapFactory.decodeResource(c.getResources(), R.drawable.notify));
        builder.setContentIntent(pendingIntent);

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        String[] descricoes = new String[]{"Perdoe-nos pelo transtorno","Estamos em fase de testes","Obrigado pela compreensão",":)"};
        for(String linha: descricoes)
            style.addLine(linha);

        builder.setStyle(style);

        Notification n = builder.build();

        n.flags = Notification.FLAG_AUTO_CANCEL;

        //espera, vibraçao, espera, vibraçao em milissegundos
        n.vibrate = new long[]{150, 100, 150, 100};

        notificationManager.notify(R.drawable.notify, n);

        Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone toque = RingtoneManager.getRingtone(c.getApplicationContext(), som);
        //toque.play();

    }

}
