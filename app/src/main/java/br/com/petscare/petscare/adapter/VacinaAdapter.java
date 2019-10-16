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
import br.com.petscare.petscare.model.Vacina;

public class VacinaAdapter extends ArrayAdapter<Vacina> {
    private Context context;
    private ArrayList<Vacina> vacinas;

    public VacinaAdapter(@NonNull Context context, @NonNull ArrayList<Vacina> objects) {
        super(context, 0, objects);
        this.context = context;
        this.vacinas = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if(vacinas != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_vacinas, parent, false);

            TextView nome = (TextView) view.findViewById(R.id.text_nome_vacina);
            TextView idade = (TextView) view.findViewById(R.id.text_idade_vacina);
            TextView periodo = (TextView) view.findViewById(R.id.text_periodo_vacina);

            Vacina vacina = vacinas.get(position);
            nome.setText("Nome: " + vacina.getNome());
            idade.setText("Idade: " + vacina.getIdade());
            periodo.setText("Período: " + vacina.getPeriodo());

        }

        return view;
    }

}
