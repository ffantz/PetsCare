package br.com.petscare.petscare.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import br.com.petscare.petscare.R;
import br.com.petscare.petscare.model.Arrays;

public class InfoActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView textoDescricao;
    private ImageView banner;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReferenceFromUrl("gs://petscare-d8b82.appspot.com/");

        if(bundle != null){
            String id = bundle.getString("id");
            int idAnimal = Integer.valueOf(id);

            textoDescricao = (TextView) findViewById(R.id.text_descricao);

            toolbar = (Toolbar) findViewById(R.id.toolbar_info);

            String tipo = bundle.getString("tipo");
            if(tipo.equals("c")) {
                String titulo = Arrays.racasCachorro[idAnimal];
                toolbar.setTitle(titulo);

                storageReference = firebaseStorage.getReferenceFromUrl("gs://petscare-d8b82.appspot.com/").child("bc" + String.valueOf(id) + ".png");

            }else{
                String titulo = Arrays.racasGatos[idAnimal];
                toolbar.setTitle(titulo);

                storageReference = firebaseStorage.getReferenceFromUrl("gs://petscare-d8b82.appspot.com/").child("bg" + String.valueOf(id) + ".png");

            }

            textoDescricao.setText(bundle.getString("descricao"));
            setSupportActionBar(toolbar);

            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    banner = (ImageView) findViewById(R.id.imagem_banner);
                    Picasso.get().load(uri.toString()).resize(800, 450).centerCrop().into(banner);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


        }else{
            finish();

        }

    }
}
