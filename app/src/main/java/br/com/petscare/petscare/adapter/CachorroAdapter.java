package br.com.petscare.petscare.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.petscare.petscare.R;
import br.com.petscare.petscare.model.Cachorro;

public class CachorroAdapter extends ArrayAdapter<Cachorro> {
    private Context context;
    private ArrayList<Cachorro> cachorros;

    public CachorroAdapter(@NonNull Context c, @NonNull ArrayList<Cachorro> objects) {
        super(c, 0, objects);
        this.context = c;
        this.cachorros = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if(cachorros != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_pets, parent, false);

            TextView especie = (TextView) view.findViewById(R.id.texto_especie_lista);
            ImageView icone = (ImageView) view.findViewById(R.id.imagem_lista_pet);

            Cachorro cachorro = cachorros.get(position);

            icone.setImageResource(cachorro.getCodigo());
            especie.setText(cachorro.getRaca());

        }

        return view;
    }
}
