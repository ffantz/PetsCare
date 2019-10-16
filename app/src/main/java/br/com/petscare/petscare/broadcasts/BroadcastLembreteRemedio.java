package br.com.petscare.petscare.broadcasts;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import br.com.petscare.petscare.R;
import br.com.petscare.petscare.activity.MainActivity;

public class BroadcastLembreteRemedio extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        gerarNotificacao(context, new Intent(context, MainActivity.class));

    }

    public void gerarNotificacao(Context context, Intent intent){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext());
        builder.setTicker("ticker texto");
        builder.setContentTitle("Pets Care");
        builder.setContentText("Não se esqueça de remediar seu Pet! ;)");
        builder.setSmallIcon(R.drawable.notify_small);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.notify));
        builder.setContentIntent(pendingIntent);

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        String[] descricoes = new String[]{"Lembrete automático","Em 10 minutos, dê remédio ao seu Pet"};
        for(String linha: descricoes)
            style.addLine(linha);

        builder.setStyle(style);

        Notification n = builder.build();

        n.flags = Notification.FLAG_AUTO_CANCEL;

        //espera, vibraçao, espera, vibraçao em milissegundos
        n.vibrate = new long[]{150, 100, 150, 100};

        notificationManager.notify(R.drawable.notify, n);

    }

}