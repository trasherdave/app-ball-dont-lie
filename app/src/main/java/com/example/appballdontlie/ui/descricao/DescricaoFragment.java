package com.example.appballdontlie.ui.descricao;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.appballdontlie.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.appballdontlie.MainActivity.arrayDados;


public class DescricaoFragment extends Fragment {
    ListView listviewDescricao;
    ArrayList<String> descricaoList = new ArrayList<>();
    Handler handler = new Handler();
    ArrayAdapter<String> listAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_descricao, container, false);
        listviewDescricao = root.findViewById(R.id.listviewDescricao);

        inicializarLista();
        new getDados().start();
        return root;

    }//onCreateView


    private void inicializarLista() {

        descricaoList = new ArrayList<>();
        listAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, descricaoList);
        listviewDescricao.setAdapter(listAdapter);

    }//inicializarLista


    public class getDados extends Thread {

        @Override
        public void run() {

            try {

                for (int i = 0; i < arrayDados.length; i++) {
                    if (arrayDados[i] != null) {

                        JSONObject jsonObject = new JSONObject(arrayDados[i]);
                        int id = jsonObject.getInt("id");
                        String data = jsonObject.getString("date");
                        String home_team = jsonObject.getJSONObject("home_team").getString("abbreviation");
                        String visitor_team = jsonObject.getJSONObject("visitor_team").getString("abbreviation");
                        descricaoList.add("Jogo: " + String.valueOf(id) + "\nData: " + data.substring(0, 11).replaceAll("-","/") + "\nTime de casa: " + home_team + " x Time visitante: " + visitor_team);

                    }//if
                }//for

            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {

                    listAdapter.notifyDataSetChanged();

                }
            });//handler

        }//run
    }//getDados
}//class


