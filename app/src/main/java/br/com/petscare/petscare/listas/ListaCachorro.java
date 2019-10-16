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
import br.com.petscare.petscare.adapter.CachorroAdapter;
import br.com.petscare.petscare.config.ConfiguracaoFirebase;
import br.com.petscare.petscare.model.Cachorro;

public class ListaCachorro extends AppCompatActivity {
    private ArrayList<Cachorro> cachorros;
    private ArrayList<Cachorro> cachorrosAux;
    private ArrayAdapter<Cachorro> adapterCachorro;

    private ListView listView;
    private Toolbar toolbar;

    private EditText editFiltro;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_cachorro);

        toolbar = (Toolbar) findViewById(R.id.toolbar_previa);
        toolbar.setTitle("Raças de cachorro");
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

        listView = (ListView) findViewById(R.id.lista_pet_cachorro);
        cachorros = new ArrayList<>();
        cachorrosAux = new ArrayList<>();
        adapterCachorro = new CachorroAdapter(ListaCachorro.this, cachorros);
        listView.setAdapter(adapterCachorro);

        databaseReference = ConfiguracaoFirebase.getReferenciaFirebase().child("cachorros");
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int contador = 0;
                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Cachorro cachorro = dados.getValue(Cachorro.class);
                    cachorro.setNumero(String.valueOf(contador));
                    cachorros.add(cachorro);
                    cachorrosAux.add(cachorro);
                    contador++;

                }

                adapterCachorro.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListaCachorro.this, InfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", cachorros.get(position).getNumero());
                bundle.putString("tipo", "c");
                bundle.putString("descricao", cachorros.get(position).getDescricao());
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }

    private void filter(String q){
        cachorros.clear();

        for (int i = 0; i < cachorrosAux.size(); i++) {
            if (cachorrosAux.get(i).getDescricao().toLowerCase().contains(q.toLowerCase())
                    || cachorrosAux.get(i).getRaca().toLowerCase().contains(q.toLowerCase())) {
                cachorros.add(cachorrosAux.get(i));

            }

        }

        adapterCachorro.notifyDataSetChanged();

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
