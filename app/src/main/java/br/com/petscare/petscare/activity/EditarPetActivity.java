package br.com.petscare.petscare.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import br.com.petscare.petscare.R;
import br.com.petscare.petscare.config.ConfiguracaoFirebase;
import br.com.petscare.petscare.helper.Preferencias;
import br.com.petscare.petscare.model.Arrays;
import br.com.petscare.petscare.model.Pet;

public class EditarPetActivity extends AppCompatActivity {
    private EditText editNome;
    private EditText editIdade;
    private RadioButton radioMacho;
    private RadioButton radioFemea;
    private RadioGroup radioGroup;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapterRaca;
    private Button botao;

    private String raca;
    private String nome;
    private String idade;
    private String sexo;
    private String tipo;
    private String id;

    private Pet pet;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pet);

        pet = (Pet) getIntent().getSerializableExtra("dados");
        tipo = pet.getTipo();
        id = pet.getId();
        Log.i("info", "id: " + "; pet.nome: " + pet.getNome());

        editNome = (EditText) findViewById(R.id.edit_nome_pet);
        editIdade = (EditText) findViewById(R.id.edit_idade_pet);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup_cadastro_pet);
        radioFemea = (RadioButton) findViewById(R.id.radio_cadastro_feminino);
        radioMacho = (RadioButton) findViewById(R.id.radio_cadastro_masculino);
        botao = (Button) findViewById(R.id.botao_salvar_pet);

        if(tipo.equals("cachorro")){
            spinner = (Spinner) findViewById(R.id.spinner_raca);
            adapterRaca = ArrayAdapter.createFromResource(this, R.array.raca_cachorro, android.R.layout.simple_spinner_dropdown_item);
            adapterRaca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapterRaca);
            spinner.setSelection(java.util.Arrays.asList(Arrays.racasCachorro).indexOf(pet.getRaca()));

        }else{
            spinner = (Spinner) findViewById(R.id.spinner_raca);
            adapterRaca = ArrayAdapter.createFromResource(this, R.array.raca_gato, android.R.layout.simple_spinner_dropdown_item);
            adapterRaca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapterRaca);
            spinner.setSelection(java.util.Arrays.asList(Arrays.racasGatos).indexOf(pet.getRaca()));

        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(tipo.equals("cachorro")){
                    raca = Arrays.racasCachorro[position];

                }else{
                    raca = Arrays.racasGatos[position];

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editNome.setText(pet.getNome());
        editIdade.setText(pet.getIdade());

        if(pet.getSexo().equals("Macho"))
            radioGroup.check(R.id.radio_cadastro_masculino);

        else
            radioGroup.check(R.id.radio_cadastro_feminino);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nome = editNome.getText().toString();
                idade = editIdade.getText().toString();

                if(radioMacho.isChecked())
                    sexo = "Macho";

                if(radioFemea.isChecked())
                    sexo = "Fêmea";

                if(!nome.isEmpty() && !idade.isEmpty() && !sexo.isEmpty() && !raca.isEmpty()) {
                    pet.setNome(nome);
                    pet.setIdade(idade);
                    pet.setRaca(raca);
                    pet.setSexo(sexo);

                    editarPet(pet);

                }else{
                    Toast.makeText(EditarPetActivity.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void editarPet(Pet pet){
        Preferencias preferencias = new Preferencias(EditarPetActivity.this);
        String identificadorUsuario = preferencias.getIdentificador();

        databaseReference = ConfiguracaoFirebase.getReferenciaFirebase();
        databaseReference.child("pets").child(identificadorUsuario).child(pet.getId()).setValue(pet);
        databaseReference.child("pets").child(identificadorUsuario).child(pet.getId()).child("id").removeValue();

        Toast.makeText(EditarPetActivity.this, "Pet editado com sucesso!", Toast.LENGTH_SHORT).show();
        finish();

    }
}
