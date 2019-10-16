package br.com.petscare.petscare.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import br.com.petscare.petscare.R;
import br.com.petscare.petscare.config.ConfiguracaoFirebase;
import br.com.petscare.petscare.helper.Notificacoes;
import br.com.petscare.petscare.model.Pet;

public class MainPetActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Pet pet;

    public static final int REQUEST_INFO_DELETE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pet);

        pet = (Pet) getIntent().getSerializableExtra("dados");

        toolbar = findViewById(R.id.toolbar_pet);
        toolbar.setTitle(pet.getNome());
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);


    }

    public void sair(){
        FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        firebaseAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pet, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.item_configuracoes:
                Toast.makeText(MainPetActivity.this, "Função ainda não implementada", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.item_sobre:
                Intent intent = new Intent(MainPetActivity.this, SobreActivity.class);
                startActivity(intent);
                return true;

            case R.id.item_sair:
                sair();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_INFO_DELETE)
            finish();
    }

    public void abrirTelaInformacoes(View v){
        Intent intent = new Intent(MainPetActivity.this, InfoPetActivity.class);
        intent.putExtra("dados", pet);
        startActivityForResult(intent, REQUEST_INFO_DELETE);

    }

    public void abrirTelaHorariosAlimentos(View v){
        Intent intent = new Intent(MainPetActivity.this, LembreteAlimentoActivity.class);
        startActivity(intent);

    }

    public void abrirTelaRemedios(View v){
        Intent intent = new Intent(MainPetActivity.this, LembreteRemedioActivity.class);
        startActivity(intent);

    }

    public void abrirTelaVacinas(View v){
        Intent intent = new Intent(MainPetActivity.this, VacinasActivity.class);
        intent.putExtra("tipo", pet.getTipo());
        startActivity(intent);

    }

    public void abrirTelaVeterinarios(View v){
        Intent intent = new Intent(MainPetActivity.this, MapsActivity.class);
        startActivity(intent);

    }

    public void abrirTelaTosa(View v){
        Intent intent = new Intent(MainPetActivity.this, LembreteTosaActivity.class);
        startActivity(intent);

    }
}
