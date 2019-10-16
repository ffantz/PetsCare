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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.petscare.petscare.R;
import br.com.petscare.petscare.config.ConfiguracaoFirebase;
import br.com.petscare.petscare.helper.Base64Custom;
import br.com.petscare.petscare.helper.Preferencias;
import br.com.petscare.petscare.model.Cachorro;
import br.com.petscare.petscare.model.Gato;
import br.com.petscare.petscare.model.Usuario;
import br.com.petscare.petscare.model.Vacina;

public class LoginActivity extends AppCompatActivity {
    private Usuario usuario;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private ValueEventListener valueEventListener;

    private Button botaoLogin;
    private EditText editEmail;
    private EditText editSenha;

    private String identificadorUsuario;

    private ArrayList<Cachorro> cachorros;
    private ArrayList<Gato> gatos;
    private ArrayList<Vacina> vacinas;

    private Cachorro cachorro;
    private Gato gato;
    private Vacina vacina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseReference = ConfiguracaoFirebase.getReferenciaFirebase();

        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        verificarUsuarioLogado();

        botaoLogin = (Button) findViewById(R.id.botaoLogar);
        editEmail = (EditText) findViewById(R.id.edit_login_email);
        editSenha = (EditText) findViewById(R.id.edit_login_senha);

        botaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String senha = editSenha.getText().toString();

                if(!email.isEmpty() && !senha.isEmpty()){
                    usuario = new Usuario();
                    usuario.setEmail(email);
                    usuario.setSenha(senha);

                    validarLogin();

                }else{
                    Toast.makeText(LoginActivity.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void verificarUsuarioLogado(){
        if(firebaseAuth.getCurrentUser() != null)
            abrirTelaPrincipal();

    }

    private void validarLogin(){
        firebaseAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                            databaseReference = ConfiguracaoFirebase.getReferenciaFirebase();
                            databaseReference.child("usuarios").child(identificadorUsuario);

                            valueEventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Usuario usuarioLogado = dataSnapshot.getValue(Usuario.class);

                                    Preferencias preferencias = new Preferencias(LoginActivity.this);
                                    preferencias.salvarDados(identificadorUsuario, usuarioLogado.getNome());

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };

                            databaseReference.addListenerForSingleValueEvent(valueEventListener);

                            Preferencias preferencias = new Preferencias(LoginActivity.this);
                            preferencias.salvarDados(identificadorUsuario, "");

                            Toast.makeText(LoginActivity.this, "Sucesso ao fazer login", Toast.LENGTH_SHORT).show();
                            abrirTelaPrincipal();

                        }else{
                            String erro;

                            try{
                                throw task.getException();

                            }catch (FirebaseAuthInvalidUserException e){
                                erro = "Email inválido.";

                            }catch (FirebaseAuthUserCollisionException e){
                                erro = "A conta já está sendo usada.";

                            }catch (FirebaseAuthInvalidCredentialsException e){
                                erro = "Credenciais inválidas.";

                            }catch (Exception e){
                                erro = "Erro ao fazer login.";
                                e.printStackTrace();

                            }

                            Toast.makeText(LoginActivity.this, erro, Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

    public void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    public void cadastroUsuario(View view){
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);

    }

    public void mostrarLista(View view){
        Intent intent = new Intent(LoginActivity.this, PreviaActivity.class);
        startActivity(intent);

    }
}
