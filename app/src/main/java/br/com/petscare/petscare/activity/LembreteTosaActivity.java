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
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.Calendar;

import br.com.petscare.petscare.R;

public class LembreteTosaActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private Button botao;

    private Calendar dataAtual;

    private EditText editHorario;
    private EditText editData;

    private SimpleMaskFormatter simpleMaskFormatter;
    private MaskTextWatcher maskTextWatcher;

    private SimpleMaskFormatter simpleMaskFormatter2;
    private MaskTextWatcher maskTextWatcher2;

    private int hora, minuto;
    private int dia, mes, ano;

    private boolean alarmeAtivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lembrete_tosa);

        alarmeAtivo = true;
        dataAtual = Calendar.getInstance();

        dataAtual.setTimeInMillis(System.currentTimeMillis());

        toolbar = findViewById(R.id.toolbar_info_pet);
        toolbar.setTitle("Lembretes de Tosa");

        editHorario = (EditText) findViewById(R.id.editText_horario_tosa);
        simpleMaskFormatter = new SimpleMaskFormatter("NN:NN");
        maskTextWatcher = new MaskTextWatcher(editHorario, simpleMaskFormatter);
        editHorario.addTextChangedListener(maskTextWatcher);

        editData = (EditText) findViewById(R.id.editText_data_tosa);
        simpleMaskFormatter2 = new SimpleMaskFormatter("NN/NN/NNNN");
        maskTextWatcher2 = new MaskTextWatcher(editData, simpleMaskFormatter2);
        editData.addTextChangedListener(maskTextWatcher2);

        botao = (Button) findViewById(R.id.button_adicionar_lembrete_tosa);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editData.getText().toString().equals("") && !editHorario.getText().toString().equals("")){
                    if(validarData()) {
                        validarAlarme();
                        finish();

                    }else{
                        Toast.makeText(LembreteTosaActivity.this, "Insira uma data válida.", Toast.LENGTH_SHORT).show();

                    }

                }else{
                    Toast.makeText(LembreteTosaActivity.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    public boolean validarData(){
        String data = editData.getText().toString();
        String[] datas = data.split("/");
        dia = Integer.valueOf(datas[0]);
        mes = Integer.valueOf(datas[1]);
        ano = Integer.valueOf(datas[2]);

        int diaAtual, mesAtual, anoAtual;
        diaAtual = dataAtual.get(Calendar.DAY_OF_MONTH);
        mesAtual = dataAtual.get(Calendar.MONTH) + 1;
        anoAtual = dataAtual.get(Calendar.YEAR);

        if(ano == anoAtual){
            if(mes == mesAtual){
                if(dia >= diaAtual && dia < 32){
                    return true;

                }else{
                    return false;

                }

            }else if(mes > mesAtual && mes < 13){
                //todos os dias liberados
                return true;

            }else if(mes < mesAtual){
                return false;

            }

        }else if(ano > anoAtual && ano < 2115){
            //todos os dias e meses liberados
            return true;

        }else if(ano < anoAtual){
            return false;

        }

        String horario = editHorario.getText().toString();
        String[] horas = horario.split(":");
        hora = Integer.valueOf(horas[0]) - 2;
        minuto = Integer.valueOf(horas[1]);

        if(hora >= 0 && hora < 25 && minuto >= 0 && minuto < 61)
            return true;

        return false;

    }

    public void validarAlarme(){
        String horario = editHorario.getText().toString();
        String[] horas = horario.split(":");
        hora = Integer.valueOf(horas[0]) - 2;
        minuto = Integer.valueOf(horas[1]);

        if(alarmeAtivo) {
            Intent intent = new Intent("ALARME_LEMBRETE_TOSA");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, hora);
            calendar.set(Calendar.MINUTE, minuto);
            calendar.set(Calendar.MONTH, mes - 1);
            calendar.set(Calendar.DAY_OF_MONTH, dia);
            calendar.set(Calendar.YEAR, ano);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            Toast.makeText(this, "Alarme para tosa definido para " + dia + "/"
                    + mes + "/" + ano + ", as " + hora + ":" + minuto, Toast.LENGTH_SHORT).show();

        }

    }
}
