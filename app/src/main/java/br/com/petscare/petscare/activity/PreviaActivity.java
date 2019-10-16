package br.com.petscare.petscare.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.petscare.petscare.adapter.CachorroAdapter;
import br.com.petscare.petscare.adapter.GatoAdapter;
import br.com.petscare.petscare.R;
import br.com.petscare.petscare.config.ConfiguracaoFirebase;
import br.com.petscare.petscare.listas.ListaCachorro;
import br.com.petscare.petscare.listas.ListaGato;
import br.com.petscare.petscare.model.Cachorro;
import br.com.petscare.petscare.model.Gato;

public class PreviaActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previa);

        toolbar = (Toolbar) findViewById(R.id.toolbar_previa);

        toolbar.setTitle("Lista de raças");
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_previa, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.item_configuracoes:
                Toast.makeText(PreviaActivity.this, "Função ainda não implementada", Toast.LENGTH_SHORT);
                return true;

            case R.id.item_sobre:
                Intent intent = new Intent(PreviaActivity.this, SobreActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    public void abrirListaCachorro(View view){
        Intent intent = new Intent(PreviaActivity.this, ListaCachorro.class);
        startActivity(intent);

    }

    public void abrirListaGato(View view){
        Intent intent = new Intent(PreviaActivity.this, ListaGato.class);
        startActivity(intent);

    }
}
