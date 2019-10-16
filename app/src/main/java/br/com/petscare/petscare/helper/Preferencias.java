package br.com.petscare.petscare.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Preferencias {
    private Context contexto;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private final String NOME_ARQUIVO = "pets.preferences";
    private final String CHAVE_NOME = "nome";
    private final String CHAVE_IDENTIFICADOR = "identificadorUsuario";
    private final int MODE = 0;

    public Preferencias(Context c){
        this.contexto = c;
        sharedPreferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = sharedPreferences.edit();

    }

    public void salvarDados(String idUsuario, String nome){
        editor.putString(CHAVE_NOME, nome);
        editor.putString(CHAVE_IDENTIFICADOR, idUsuario);
        editor.commit();

    }

    public String getIdentificador(){
        return sharedPreferences.getString(CHAVE_IDENTIFICADOR, null);

    }

    public String getNome(){
        return sharedPreferences.getString(CHAVE_NOME, null);

    }


}
