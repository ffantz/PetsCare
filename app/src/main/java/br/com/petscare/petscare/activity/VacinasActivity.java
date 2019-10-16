package br.com.petscare.petscare.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.petscare.petscare.R;
import br.com.petscare.petscare.adapter.VacinaAdapter;
import br.com.petscare.petscare.config.ConfiguracaoFirebase;
import br.com.petscare.petscare.model.Vacina;

public class VacinasActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    private Toolbar toolbar;
    private String tipo;

    private ListView listView;
    private ArrayList<Vacina> vacinas;
    private ArrayAdapter<Vacina> adapterVacina;

    private Button botao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacinas);

        toolbar = findViewById(R.id.toolbar_info);
        toolbar.setTitle("Cartela de vacinação e lembretes");

        tipo = (String) getIntent().getSerializableExtra("tipo");

        listView = (ListView) findViewById(R.id.listView_vacinas);
        botao = (Button) findViewById(R.id.button_configurar_lembrete_vacina);

        vacinas = new ArrayList<>();
        adapterVacina = new VacinaAdapter(getApplicationContext(), vacinas);
        listView.setAdapter(adapterVacina);

        buscarVacinas();

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacinasActivity.this, LembreteVacinaActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    private void buscarVacinas(){
        databaseReference = ConfiguracaoFirebase.getReferenciaFirebase().child("vacinas");
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildren() != null){
                    vacinas.clear();

                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        if(data != null) {
                            Vacina vacina = data.getValue(Vacina.class);
                            Log.i("dados", "tipo: " + tipo + " - vacina: " + vacina.getTipo());
                            if(vacina.getTipo().equals(tipo))
                                vacinas.add(vacina);

                        }

                    }

                    adapterVacina.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addListenerForSingleValueEvent(valueEventListener);


    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListener);
    }

}
