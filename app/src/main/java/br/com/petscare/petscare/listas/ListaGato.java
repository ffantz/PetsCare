package br.com.petscare.petscare.listas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.petscare.petscare.R;
import br.com.petscare.petscare.activity.InfoActivity;
import br.com.petscare.petscare.adapter.GatoAdapter;
import br.com.petscare.petscare.config.ConfiguracaoFirebase;
import br.com.petscare.petscare.model.Gato;

public class ListaGato extends AppCompatActivity {
    private ArrayList<Gato> gatos;
    private ArrayList<Gato> gatosAux;
    private ArrayAdapter<Gato> adapterGato;

    private ListView listView;
    private Toolbar toolbar;

    private EditText editFiltro;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_gato);

        toolbar = (Toolbar) findViewById(R.id.toolbar_previa);
        toolbar.setTitle("Raças de gato");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        editFiltro = (EditText) findViewById(R.id.editFiltro);
        editFiltro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().equals(""))
                    filter(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        listView = (ListView) findViewById(R.id.lista_pet_gato);
        gatos = new ArrayList<>();
        gatosAux = new ArrayList<>();
        adapterGato = new GatoAdapter(ListaGato.this, gatos);
        listView.setAdapter(adapterGato);

        databaseReference = ConfiguracaoFirebase.getReferenciaFirebase().child("gatos");
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int contador = 0;
                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Gato gato = dados.getValue(Gato.class);
                    gato.setNumero(String.valueOf(contador));
                    gatos.add(gato);
                    gatosAux.add(gato);
                    contador++;

                }

                adapterGato.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListaGato.this, InfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", gatos.get(position).getNumero());
                bundle.putString("tipo", "g");
                bundle.putString("descricao", gatos.get(position).getDescricao());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void filter(String q){
        gatos.clear();

        for (int i = 0; i < gatosAux.size(); i++) {
            if (gatosAux.get(i).getDescricao().toLowerCase().contains(q.toLowerCase())
                    || gatosAux.get(i).getRaca().toLowerCase().contains(q.toLowerCase())) {
                gatos.add(gatosAux.get(i));

            }

        }

        adapterGato.notifyDataSetChanged();

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
