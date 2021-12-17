package com.example.appballdontlie.ui.ganhador;

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

import com.example.appballdontlie.MainActivity;
import com.example.appballdontlie.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.appballdontlie.MainActivity.arrayDados;

public class GanhadorFragment extends Fragment {
    ListView listviewGanhador;
    ArrayList<String> ganhadoresList = new ArrayList<>();
    Handler handler = new Handler();
    ArrayAdapter<String> listAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_ganhador, container, false);
        listviewGanhador = root.findViewById(R.id.listviewGanhador);
        inicializarLista();
        new getDados().start();
        return root;

    }//onCreateView

    private void inicializarLista() {

        ganhadoresList = new ArrayList<>();
        listAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, ganhadoresList);
        listviewGanhador.setAdapter(listAdapter);

    }//inicializarLista


    public class getDados extends Thread {


        @Override
        public void run() {

            try {

                for (int i = 0; i < arrayDados.length; i++) {

                    if (arrayDados[i] != null) {

                        JSONObject jsonObject = new JSONObject(arrayDados[i]);
                        String timeCasa = jsonObject.getJSONObject("home_team").getString("full_name");
                        int pontosCasa = jsonObject.getInt("home_team_score");
                        String timeVisitante = jsonObject.getJSONObject("visitor_team").getString("full_name");
                        int pontosVisitante = jsonObject.getInt("visitor_team_score");

                        if (pontosCasa > pontosVisitante) {

                            ganhadoresList.add(" " + timeCasa);

                        } else {

                            ganhadoresList.add(" " + timeVisitante);

                        }

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