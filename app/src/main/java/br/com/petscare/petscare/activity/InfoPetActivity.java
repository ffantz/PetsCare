package br.com.petscare.petscare.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.petscare.petscare.R;
import br.com.petscare.petscare.config.ConfiguracaoFirebase;
import br.com.petscare.petscare.helper.Preferencias;
import br.com.petscare.petscare.model.Pet;

public class InfoPetActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Pet pet;

    private TextView textNome;
    private TextView textIdade;
    private TextView textRaca;
    private TextView textSexo;

    private DatabaseReference databaseReference;

    private String identificador;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_pet);

        toolbar = (Toolbar) findViewById(R.id.toolbar_info_pet);
        toolbar.setTitle("Informações");
        setSupportActionBar(toolbar);

        pet = (Pet) getIntent().getSerializableExtra("dados");
        id = pet.getId();

        Preferencias preferencias = new Preferencias(InfoPetActivity.this);
        identificador = preferencias.getIdentificador();

        atualizarInfo();

    }

    public void atualizarInfo(){
        pet = (Pet) getIntent().getSerializableExtra("dados");

        textNome = (TextView) findViewById(R.id.info_nome_pet);
        textIdade = (TextView) findViewById(R.id.info_idade_pet);
        textRaca = (TextView) findViewById(R.id.info_raca_pet);
        textSexo = (TextView) findViewById(R.id.info_sexo_pet);

        textNome.setText(pet.getNome());
        textIdade.setText(pet.getIdade());
        textRaca.setText(pet.getRaca());
        textSexo.setText(pet.getSexo());

    }

    public void atualizarFirebase(){

        databaseReference = ConfiguracaoFirebase.getReferenciaFirebase().child("pets").child(identificador).child(pet.getId());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildren() != null){
                    pet = dataSnapshot.getValue(Pet.class);

                    textNome = (TextView) findViewById(R.id.info_nome_pet);
                    textIdade = (TextView) findViewById(R.id.info_idade_pet);
                    textRaca = (TextView) findViewById(R.id.info_raca_pet);
                    textSexo = (TextView) findViewById(R.id.info_sexo_pet);

                    textNome.setText(pet.getNome());
                    textIdade.setText(pet.getIdade());
                    textRaca.setText(pet.getRaca());
                    textSexo.setText(pet.getSexo());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void excluirPet(Pet pet){
        pet = (Pet) getIntent().getSerializableExtra("dados");

        Preferencias preferencias = new Preferencias(InfoPetActivity.this);
        String identificador = preferencias.getIdentificador();

        databaseReference = ConfiguracaoFirebase.getReferenciaFirebase().child("pets").child(identificador).child(pet.getId());
        databaseReference.removeValue();
        setResult(RESULT_OK, new Intent());
        finish();

    }

    @Override
    protected void onStart() {
        super.onStart();
        atualizarInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarFirebase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_info_pet, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()){
            case R.id.item_edit:
                intent = new Intent(InfoPetActivity.this, EditarPetActivity.class);
                pet.setId(id);
                intent.putExtra("dados", pet);
                Log.i("info", "id: " + id + "; pet.id: " + pet.getId());
                startActivityForResult(intent, 1001);
                return true;

            case R.id.item_delete:
                excluirPet(pet);
                Toast.makeText(InfoPetActivity.this, "Pet removido com sucesso!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, new Intent());
                finish();
                return true;

            case R.id.item_configuracoes:
                Toast.makeText(InfoPetActivity.this, "Função ainda não implementada", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.item_sobre:
                intent = new Intent(InfoPetActivity.this, SobreActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }
}
