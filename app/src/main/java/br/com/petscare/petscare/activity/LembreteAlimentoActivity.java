package br.com.petscare.petscare.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

import br.com.petscare.petscare.R;

public class LembreteAlimentoActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button botao;
    private boolean alarmeAtivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lembrete_alimento);

        toolbar = findViewById(R.id.toolbar_pet);
        toolbar.setTitle("Lembretes de alimentação");

        botao = (Button) findViewById(R.id.button_ativar_lembretes_alimento);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarAlarme();
                finish();

            }
        });

    }

    public void validarAlarme(){
        alarmeAtivo = (PendingIntent.getBroadcast(this, 0, new Intent("ALARME_LEMBRETE_ALIMENTO"), PendingIntent.FLAG_NO_CREATE) == null);

        if(alarmeAtivo) {
            Intent intent = new Intent("ALARME_LEMBRETE_ALIMENTO");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 6);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 43200000, pendingIntent);

            Toast.makeText(this, "Lembretes definidos para 6h e 18h", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "Lembretes já configurados para os horários: 6h e 18h", Toast.LENGTH_SHORT).show();


        }


    }
}
