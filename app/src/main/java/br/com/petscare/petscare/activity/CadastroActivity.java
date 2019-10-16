package br.com.petscare.petscare.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.com.petscare.petscare.R;
import br.com.petscare.petscare.config.ConfiguracaoFirebase;
import br.com.petscare.petscare.helper.Base64Custom;
import br.com.petscare.petscare.helper.Preferencias;
import br.com.petscare.petscare.model.Usuario;

public class CadastroActivity extends AppCompatActivity {
    private Button botaoCadastro;
    private EditText editNome;
    private EditText editEmail;
    private EditText editSenha;

    private Usuario usuario;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();

        botaoCadastro = (Button) findViewById(R.id.botaoCadastrar);
        editEmail = (EditText) findViewById(R.id.edit_cadastro_email);
        editNome = (EditText) findViewById(R.id.edit_cadastro_nome);
        editSenha = (EditText) findViewById(R.id.edit_cadastro_senha);

        botaoCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = editNome.getText().toString();
                String senha = editSenha.getText().toString();
                String email = editEmail.getText().toString();

                if(!nome.isEmpty() && !senha.isEmpty() && !email.isEmpty()){
                    usuario = new Usuario();
                    usuario.setEmail(email);
                    usuario.setNome(nome);
                    usuario.setSenha(senha);
                    cadastrarUsuario();

                }else{
                    Toast.makeText(CadastroActivity.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void cadastrarUsuario(){
        firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(CadastroActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                            String emailCodificado = Base64Custom.codificarBase64(usuario.getEmail());
                            usuario.setId(emailCodificado);
                            usuario.salvarUsuario();

                            Preferencias preferencias = new Preferencias(CadastroActivity.this);
                            preferencias.salvarDados(usuario.getId(), usuario.getNome());

                            abrirUsuarioLogado();

                        }else{
                            String erro;

                            try{
                                throw task.getException();

                            } catch (FirebaseAuthWeakPasswordException e) {
                                erro = "Digite uma senha mais forte, com o mínimo de 6 caracteres.";

                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erro = "Digite um email válido.";

                            } catch (FirebaseAuthUserCollisionException e) {
                                erro = "Email já cadastrado.";

                            } catch (Exception e){
                                erro = "Erro ao cadastrar Usuário.";
                                e.printStackTrace();

                            }

                            Toast.makeText(getApplicationContext(), erro, Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

    private void abrirUsuarioLogado(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();

    }
}
