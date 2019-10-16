package br.com.petscare.petscare.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.Calendar;

import br.com.petscare.petscare.R;

public class LembreteRemedioActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private Button botao;

    private EditText editHorario;
    private EditText editHora;
    private CheckBox checkBox;

    private SimpleMaskFormatter simpleMaskFormatter;
    private MaskTextWatcher maskTextWatcher;

    private SimpleMaskFormatter simpleMaskFormatter2;
    private MaskTextWatcher maskTextWatcher2;

    private int hora, minuto;

    private boolean alarmeAtivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lembrete_remedio);

        alarmeAtivo = true;

        toolbar = findViewById(R.id.toolbar_info_pet);
        toolbar.setTitle("Lembretes de Remédios");

        editHorario = (EditText) findViewById(R.id.editText_horario_remedio);
        simpleMaskFormatter = new SimpleMaskFormatter("NN:NN");
        maskTextWatcher = new MaskTextWatcher(editHorario, simpleMaskFormatter);
        editHorario.addTextChangedListener(maskTextWatcher);

        editHora = (EditText) findViewById(R.id.editText_horas_repeticao);
        simpleMaskFormatter2 = new SimpleMaskFormatter("N");
        maskTextWatcher2 = new MaskTextWatcher(editHora, simpleMaskFormatter2);
        editHora.addTextChangedListener(maskTextWatcher2);

        checkBox = (CheckBox) findViewById(R.id.check_remedio);

        botao = (Button) findViewById(R.id.button_adicionar_lembrete_remedio);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editHorario.getText().toString().equals("")) {
                    if (checkBox.isChecked()) {
                        if (!editHora.getText().toString().equals("")) {
                            if(validarHorario()) {
                                if(validarHora()) {
                                    validarAlarme(true);
                                    finish();

                                }

                            }

                        } else {
                            Toast.makeText(LembreteRemedioActivity.this, "Informe a repetição ou desmarque a opção", Toast.LENGTH_SHORT).show();

                        }

                    } else {
                        if(validarHorario()) {
                            validarAlarme(false);
                            finish();

                        }

                    }

                }else{
                    Toast.makeText(LembreteRemedioActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    public boolean validarHorario(){
        String horario = editHorario.getText().toString();
        String[] horas = horario.split(":");
        hora = Integer.valueOf(horas[0]);
        minuto = Integer.valueOf(horas[1]);

        if(minuto < 0 || minuto > 59){
            Toast.makeText(this, "Insira uma data válida", Toast.LENGTH_SHORT).show();
            return false;

        }

        if(hora < 0 || hora > 24){
            Toast.makeText(this, "Insira uma data válida", Toast.LENGTH_SHORT).show();
            return false;

        }

        return true;
    }

    public boolean validarHora(){
        int hora = Integer.valueOf(editHora.getText().toString());

        if(hora < 0 || hora > 24){
            Toast.makeText(this, "Insira uma data válida", Toast.LENGTH_SHORT).show();
            return false;

        }

        return true;
    }

    public void validarAlarme(boolean repetir){
        String horario = editHorario.getText().toString();
        String[] horas = horario.split(":");
        hora = Integer.valueOf(horas[0]);
        minuto = Integer.valueOf(horas[1]);

        if(minuto < 10){
            minuto += 60 - 10;
            hora--;

        }else{
            minuto -= 10;

        }

        if(alarmeAtivo) {
            Intent intent = new Intent("ALARME_LEMBRETE_REMEDIO");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hora);
            calendar.set(Calendar.MINUTE, minuto);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            if(repetir) {
                long tempo = Long.valueOf(editHora.getText().toString());
                tempo = tempo * 60 * 60 * 1000;
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), tempo, pendingIntent);

            }

            Toast.makeText(this, "Alarme do remédio definido para " + hora + ":" + minuto, Toast.LENGTH_SHORT).show();

        }

    }
}
