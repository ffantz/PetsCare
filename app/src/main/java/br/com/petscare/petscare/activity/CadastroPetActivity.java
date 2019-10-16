package br.com.petscare.petscare.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class CadastroPetActivity extends AppCompatActivity {
    private  DatabaseReference databaseReference;

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

    private Pet pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pet);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            final String tipo = bundle.getString("tipo");

            editNome = (EditText) findViewById(R.id.edit_nome_pet);
            editIdade = (EditText) findViewById(R.id.edit_idade_pet);
            radioGroup = (RadioGroup) findViewById(R.id.radioGroup_cadastro_pet);
            radioFemea = (RadioButton) findViewById(R.id.radio_cadastro_feminino);
            radioMacho = (RadioButton) findViewById(R.id.radio_cadastro_masculino);
            botao = (Button) findViewById(R.id.botao_cadastrar_pet);

            databaseReference = ConfiguracaoFirebase.getReferenciaFirebase();

            if(tipo.equals("c")){
                spinner = (Spinner) findViewById(R.id.spinner_raca);
                adapterRaca = ArrayAdapter.createFromResource(this, R.array.raca_cachorro, android.R.layout.simple_spinner_dropdown_item);
                adapterRaca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapterRaca);

            }else{
                spinner = (Spinner) findViewById(R.id.spinner_raca);
                adapterRaca = ArrayAdapter.createFromResource(this, R.array.raca_gato, android.R.layout.simple_spinner_dropdown_item);
                adapterRaca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapterRaca);

            }

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(tipo.equals("c")){
                        raca = Arrays.racasCachorro[position];

                    }else{
                        raca = Arrays.racasGatos[position];

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            botao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nome = editNome.getText().toString();
                    idade = editIdade.getText().toString();

                    if(radioMacho.isChecked())
                        sexo = "Macho";

                    if(radioFemea.isChecked())
                        sexo = "Fêmea";

                    if(!nome.isEmpty() && !idade.isEmpty() && !sexo.isEmpty() && !raca.isEmpty()){
                        pet = new Pet();
                        pet.setNome(nome);
                        pet.setIdade(idade);
                        pet.setRaca(raca);
                        pet.setSexo(sexo);

                        if(tipo.equals("c"))
                            pet.setTipo("cachorro");

                        else
                            pet.setTipo("gato");

                        cadastrarPet(pet);

                    }else{
                        Toast.makeText(CadastroPetActivity.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();

                    }

                }
            });

        }else{
            Intent intentRetorno = new Intent(CadastroPetActivity.this, MainActivity.class);
            startActivity(intentRetorno);
            finish();

        }
    }

    private void cadastrarPet(Pet pet){
        Preferencias preferencias = new Preferencias(CadastroPetActivity.this);
        String identificadorUsuario = preferencias.getIdentificador();

        databaseReference.child("pets").child(identificadorUsuario).push().setValue(pet);

        Toast.makeText(CadastroPetActivity.this, "Pet cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
        Intent intentRetorno = new Intent(CadastroPetActivity.this, MainActivity.class);
        startActivity(intentRetorno);
        finish();

    }

}
