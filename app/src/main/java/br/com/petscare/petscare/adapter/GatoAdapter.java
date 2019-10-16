package br.com.petscare.petscare.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.petscare.petscare.R;
import br.com.petscare.petscare.model.Gato;

public class GatoAdapter extends ArrayAdapter<Gato>{
    private Context context;
    private ArrayList<Gato> gatos;

    public GatoAdapter(@NonNull Context c, @NonNull ArrayList<Gato> objects) {
        super(c, 0, objects);
        this.context = c;
        this.gatos = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if(gatos != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_pets, parent, false);

            TextView especie = (TextView) view.findViewById(R.id.texto_especie_lista);
            ImageView icone = (ImageView) view.findViewById(R.id.imagem_lista_pet);

            Gato gato = gatos.get(position);

            icone.setImageResource(gato.getCodigo());
            especie.setText(gato.getRaca());

        }

        return view;
    }
}
