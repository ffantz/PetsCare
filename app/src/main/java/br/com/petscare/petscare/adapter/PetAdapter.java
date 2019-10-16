package br.com.petscare.petscare.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.petscare.petscare.R;
import br.com.petscare.petscare.model.Pet;

public class PetAdapter extends ArrayAdapter<Pet> {
    private Context context;
    private ArrayList<Pet> pets;

    public PetAdapter(@NonNull Context c, @NonNull ArrayList<Pet> objects) {
        super(c, 0, objects);
        this.context = c;
        this.pets = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if(pets != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_pets_cadastrados, parent, false);

            TextView nome = (TextView) view.findViewById(R.id.text_nome_pet);
            TextView raca = (TextView) view.findViewById(R.id.text_raca_pet);
            TextView tipo = (TextView) view.findViewById(R.id.text_tipo_pet);

            Pet pet = pets.get(position);
            nome.setText(pet.getNome());
            raca.setText(pet.getRaca());
            tipo.setText(pet.getTipo());

        }

        return view;
    }

}
