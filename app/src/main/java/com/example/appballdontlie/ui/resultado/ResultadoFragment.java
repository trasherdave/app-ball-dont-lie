package com.example.appballdontlie.ui.resultado;

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
import static com.example.appballdontlie.MainActivity.arrayResultados;

public class ResultadoFragment extends Fragment {
    ListView listviewResultado;
    ArrayList<String> resultadoList = new ArrayList<>();
    Handler handler = new Handler();
    ArrayAdapter<String> listAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_resultado, container, false);
        listviewResultado = root.findViewById(R.id.listviewResultado);

        inicializarLista();
        new getDados().start();
        return root;

    }//onCreateView

    private void inicializarLista() {

        resultadoList = new ArrayList<>();
        listAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, resultadoList);
        listviewResultado.setAdapter(listAdapter);

    }//inicializarLista


    public class getDados extends Thread {

        @Override
        public void run() {

            try {

                for (int i = 0; i < arrayDados.length; i++) {
                    if (arrayDados[i] != null) {

                        JSONObject jsonObject = new JSONObject(arrayDados[i]);
                        String pontosCasa = jsonObject.getString("home_team_score");
                        String pontosVisitante = jsonObject.getString("visitor_team_score");
                        String siglaCasa = jsonObject.getJSONObject("home_team").getString("abbreviation");
                        String siglaVisitante = jsonObject.getJSONObject("visitor_team").getString("abbreviation");
                        resultadoList.add(siglaCasa + " " + pontosCasa + " pontos" + " x " + siglaVisitante + " " + pontosVisitante + " pontos\n");

                    }//if
                }//for

                arrayResultados = new ArrayList<String>();
                arrayResultados = resultadoList;


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