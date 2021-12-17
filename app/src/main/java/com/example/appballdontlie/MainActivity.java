package com.example.appballdontlie;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static com.example.appballdontlie.ui.home.HomeFragment.text_api;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private static final int CODIGO_SOLICITACAO = 1;
    private static final String PERMISSAO = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static String[] arrayDados = new String[100];
    public static ArrayList<String> arrayResultados = new ArrayList<String>();
    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FAB
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                solicitarPermissao();

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(

                R.id.nav_home, R.id.nav_descricao, R.id.nav_time_casa, R.id.nav_time_visitante, R.id.nav_resultado, R.id.nav_ganhador)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        new receberDados().start();

    }

    private void solicitarPermissao() {

        int temPermissao = ContextCompat.checkSelfPermission(this, PERMISSAO);
        if (temPermissao != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{PERMISSAO}, CODIGO_SOLICITACAO);

        }//if
        else {

            armazenarDados();

        }//else

    }//solicitarPermissao

    private void armazenarDados() {

        String dados = "";
        if (!arrayResultados.isEmpty()) {

            dados = arrayResultados.toString();

            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                File file = new File("/sdcard/" + "meuArq.txt");
                FileOutputStream fos = null;

                try {

                    fos = new FileOutputStream(file);
                    OutputStreamWriter osw = new OutputStreamWriter(fos);
                    osw.write(dados);
                    osw.close();
                    fos.close();
                    Toast.makeText(getApplicationContext(), "Os resultados dos jogos foram salvos!", Toast.LENGTH_LONG).show();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Não há espaço", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "A lista de resultados está vazia, primeiro, acesse o item: 'Resultados dos Jogos'", Toast.LENGTH_LONG).show();
        }

    }//armazenarDados

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Permissão ok", Toast.LENGTH_SHORT).show();

            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, PERMISSAO)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Atenção").setMessage("A permissão é necessária para gravar os resultados").setCancelable(false)
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{PERMISSAO}, CODIGO_SOLICITACAO);
                                }
                            })
                            .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Toast.makeText(getApplicationContext(), "É necessária a permissão para a gravação dos resultados", Toast.LENGTH_SHORT).show();

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    finish();
                }
            }//else if
        }//if
    }//onRequestPermissionsResult


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }//onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.localizacao:
                localizacaoIFTO();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }//onOptionsItemSelected

    private void localizacaoIFTO() {

        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);


    }//localizacaoIFTO

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }//onSupportNavigateUp


    private String[] gerarNumerosAleatorios() {

        String[] url = new String[100];
        Integer[] numeros = new Integer[100];
        Random random = new Random();


        for (int i = 0; i < numeros.length; i++) {

            int numeroTmp = 1 + random.nextInt(20);
            boolean contains = Arrays.asList(numeros).contains(numeroTmp);

            if (contains) {

            } else {

                numeros[i] = numeroTmp;
                System.out.println("NUMERO > " + numeros[i]);
                url[i] = "https://www.balldontlie.io/api/v1/games/" + numeros[i];
                System.out.println("URL > " + url[i]);

            }
        }
        return url;

    }//gerarNumerosAleatorios


    public class receberDados extends Thread {

        @Override
        public void run() {

            handler.post(new Runnable() {
                @Override
                public void run() {

                    text_api.setText("Carregando...");

                }
            });

            try {

                String[] urls = new String[100];
                urls = gerarNumerosAleatorios();

                for (int i = 0; i < urls.length; i++) {

                    if (urls[i] != null) {

                        URL url = new URL(urls[i]);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String linha;


                        while ((linha = bufferedReader.readLine()) != null) {

                            arrayDados[i] = linha;
                        }
                    }//if
                }//for

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {

                    text_api.setText("API BallDontLie");

                }
            });

        }//run
    }//getDados
}//class