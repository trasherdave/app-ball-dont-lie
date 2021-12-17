package com.example.appballdontlie.ui.timeDeCasa;

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

public class TimeDeCasaFragment extends Fragment {
    ListView listviewTimeCasa;
    ArrayList<String> timeCasaList = new ArrayList<>();
    Handler handler = new Handler();
    ArrayAdapter<String> listAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_time_casa, container, false);
        listviewTimeCasa = root.findViewById(R.id.listviewTimeCasa);
        inicializarLista();
        new getDados().start();
        return root;

    }//onCreateView

    private void inicializarLista() {

        timeCasaList = new ArrayList<>();
        listAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, timeCasaList);
        listviewTimeCasa.setAdapter(listAdapter);

    }//inicializarLista


    public class getDados extends Thread {

        @Override
        public void run() {

            try {


                for (int i = 0; i < arrayDados.length; i++) {
                    if (arrayDados[i] != null) {

                        JSONObject jsonObject = new JSONObject(arrayDados[i]);
                        String nome = jsonObject.getJSONObject("home_team").getString("full_name");
                        String cidade = jsonObject.getJSONObject("home_team").getString("city");
                        timeCasaList.add("Time: " + nome + "\n" + "Cidade: " + cidade);

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
