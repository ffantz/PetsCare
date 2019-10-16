package br.com.petscare.petscare.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.petscare.petscare.R;
import br.com.petscare.petscare.adapter.PetAdapter;
import br.com.petscare.petscare.config.ConfiguracaoFirebase;
import br.com.petscare.petscare.helper.Preferencias;
import br.com.petscare.petscare.model.Pet;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    private ArrayList<Pet> pets;
    private ArrayAdapter<Pet> adapterPet;

    private ListView listView;
    private TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar_principal);
        toolbar.setTitle("Pets Care");
        setSupportActionBar(toolbar);

        texto = (TextView) findViewById(R.id.text_sem_pet);
        listView = (ListView) findViewById(R.id.pets_cadastrados);

        pets = new ArrayList<>();
        adapterPet = new PetAdapter(MainActivity.this, pets);
        listView.setAdapter(adapterPet);

        verificarPetCadastrado();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pet petAtual = (Pet) adapterPet.getItem(position);

                Intent intent = new Intent(MainActivity.this, MainPetActivity.class);
                intent.putExtra("dados", petAtual);
                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dialogoExclusao(position);

                verificarPetCadastrado();

                return true;
            }
        });

    }

    private void excluirPet(int position){
        Preferencias preferencias = new Preferencias(MainActivity.this);
        String identificador = preferencias.getIdentificador();

        Pet aux = pets.get(position);

        databaseReference = ConfiguracaoFirebase.getReferenciaFirebase().child("pets").child(identificador).child(aux.getId());
        databaseReference.removeValue();

        Toast.makeText(MainActivity.this, "Pet removido com sucesso!", Toast.LENGTH_SHORT).show();
    }

    private void verificarPetCadastrado(){
        Preferencias preferencias = new Preferencias(MainActivity.this);
        String identificador = preferencias.getIdentificador();

        databaseReference = ConfiguracaoFirebase.getReferenciaFirebase().child("pets").child(identificador);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {
                    pets.clear();

                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        if(data != null) {
                            Pet pet = data.getValue(Pet.class);
                            pet.setId(data.getKey());
                            pets.add(pet);
                        }

                    }

                    adapterPet.notifyDataSetChanged();

                    listView.setVisibility(View.VISIBLE);
                    texto.setVisibility(View.INVISIBLE);

                }else{
                    texto.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.INVISIBLE);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        databaseReference.addListenerForSingleValueEvent(valueEventListener);

    }

    public void sair(){
        FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        firebaseAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){

            case R.id.item_configuracoes:
                Toast.makeText(MainActivity.this, "Função ainda não implementada", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.item_adicionar_pet:
                dialogoCadastro();
                return true;

            case R.id.item_racas:
                intent = new Intent(MainActivity.this, PreviaActivity.class);
                startActivity(intent);
                return true;

            case R.id.item_sobre:
                 intent = new Intent(MainActivity.this, SobreActivity.class);
                startActivity(intent);
                return true;

            case R.id.item_sair:
                sair();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void dialogoExclusao(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.dialogPadrao));
        builder.setMessage("Tem certeza que deseja remover seu pet?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        excluirPet(position);

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        builder.show();

    }

    private void dialogoCadastro(){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.radiogroup_dialog);

        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radio_group_cadastro);

        dialog.show();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioCachorro = (RadioButton) group.getChildAt(0);
                RadioButton radioGato = (RadioButton) group.getChildAt(1);
                if(radioCachorro.getId() == checkedId){
                    Intent intent = new Intent(MainActivity.this, CadastroPetActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("tipo", "c");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    dialog.dismiss();

                }

                if(radioGato.getId() == checkedId){
                    Intent intent = new Intent(MainActivity.this, CadastroPetActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("tipo", "g");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    dialog.dismiss();

                }

            }
        });

    }

}
