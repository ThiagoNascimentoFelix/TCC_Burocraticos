package com.example.julieti.tcc;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.julieti.tcc.db.repositorios.PercursoRepositorio;
import com.example.julieti.tcc.modelo.Percurso;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public  class MapaActivity extends AppCompatActivity implements
        OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String EXTRA_PERCURSO = "extraPercurso";

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    private float distancia = 0;
    private Boolean validaDistancia = false;

    private LatLng cordMyLocation;
    private LatLng cordTarget;

    private Percurso mPercurso;

    private PercursoRepositorio percursoRepositorio;

    public MapaActivity(){}


    //-------------------------------MAPS------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle =  getIntent().getExtras();
        if (bundle != null) {
            mPercurso = bundle.getParcelable(EXTRA_PERCURSO);
        } else {
            mPercurso = new Percurso(0, "Mapa", null);
        }

        cordTarget = new LatLng(mPercurso.getLatitude(), mPercurso.getLongitude());

        percursoRepositorio = new PercursoRepositorio(this);

        setContentView(R.layout.activity_mapa);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        Button buttonRota = (Button) findViewById(R.id.bttracar_rota);
        buttonRota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mGoogleApiClient.isConnected()) {
                    if (validaDistancia){
                        mPercurso.setStatus("COMPLETO");
                        percursoRepositorio.salvar(mPercurso);
                        Percurso proximoPercurso = percursoRepositorio.buscarPercursoPorId(mPercurso.getId()+1);
                        if (mPercurso.getTemaId() == proximoPercurso.getTemaId()) {
                            proximoPercurso.setStatus("INCOMPLETO");
                            percursoRepositorio.salvar(proximoPercurso);
                        }
                        Intent intent = new Intent(MapaActivity.this, BuscarInformacaoActivity.class);
                        intent.putExtra(BuscarInformacaoActivity.EXTRA_PERCURSO, mPercurso);
                        startActivity(intent);
                        finish();
                    }else{
                        tracarRota();
                    }
                } else {
                    mGoogleApiClient.connect();
                }
            }
        });


    }


    // -----------------------------MENU-----------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_fixe_location:
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(cordTarget));
                CameraPosition update = new CameraPosition(cordTarget, 15, 0, 0);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(update), 3000, null);
                break;
            case R.id.action_my_location:
                //QUANDO A OPÇÃO 'MINHA LOCALIZAÇÃO' FOR CLICADA
                break;
            case R.id.action_map_types:
                if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                } else if (mMap.getMapType() == GoogleMap.MAP_TYPE_HYBRID) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                } else if (mMap.getMapType() == GoogleMap.MAP_TYPE_TERRAIN) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                } else if (mMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                } else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Location target = new Location("") ;
        target.setLatitude(cordTarget.latitude);
        target.setLongitude(cordTarget.longitude);
        distancia = lastLocation.distanceTo(target)/1000;
        if (distancia < 0.02) validaDistancia = true;

        if (lastLocation != null) {
            cordMyLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            tracarRota();
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void tracarRota() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Location target = new Location("") ;
        target.setLatitude(cordTarget.latitude);
        target.setLongitude(cordTarget.longitude);
        distancia = lastLocation.distanceTo(target)/1000;
        if (distancia < 0.03) validaDistancia = true;

        if (lastLocation != null) {
            cordMyLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        }

        Context contexto = getApplicationContext();
        String msgDistancia = "Distância até o destino: " + distancia + " metros";

        Toast toast = Toast.makeText(contexto,msgDistancia ,Toast.LENGTH_LONG);
        toast.show();

        mMap.setInfoWindowAdapter(null);
        mMap.clear();
        String url = montarURLRotaMapa(cordMyLocation.latitude,cordMyLocation.longitude
                , cordTarget.latitude, cordTarget.longitude);

        MinhaAsyncTask tarefa = new MinhaAsyncTask();

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB){
            tarefa.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        }else{
            tarefa.execute(url);
        }

        mMap.addMarker(new MarkerOptions().position(cordMyLocation));
        mMap.addMarker(new MarkerOptions().position(cordTarget));

        CameraPosition updateRota = new CameraPosition(cordMyLocation, 15, 0, 0);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(updateRota), 3000, null);
    }


    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermitirLocalizacaoActivity.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermitirLocalizacaoActivity.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            enableMyLocation();
        } else {
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermitirLocalizacaoActivity.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    // ------------------------------------ TRAÇAR ROTAS ----------------------------------------

    private String montarURLRotaMapa(double latOrigen, double lngOrigen, double latDestino, double lngDestino){
        //Base da URL
        String url = "http://maps.googleapis.com/maps/api/directions/json?origin=";
        //Local de origem
        url += latOrigen + "," + lngOrigen;
        url += "&destination=";
        //Local de destino
        url += latDestino + "," + lngDestino;
        //Outros parametros
        url += "&sensor=false&mode=driving&alternatives=true";

        return url;
    }

    public JSONObject requisicaoHTTP(String url) {
        JSONObject resultado = null;

        try {
            //Criamos um cliente HTTP para que possamos realizar a
            //requisição a um Servidor HTTP
            DefaultHttpClient httpClient = new DefaultHttpClient();
            //Definimos o método de requisição como sendo POST
            HttpPost httpPost = new HttpPost(url);
            //Recuperamos a resposta do Servidor HTTP
            HttpResponse httpResponse = httpClient.execute(httpPost);
            //Recuperamos o conteúdo enviado do Servidor HTTP
            HttpEntity httpEntity = httpResponse.getEntity();
            //Transformamos tal conteúdo em 'String'
            String conteudo = EntityUtils.toString(httpEntity);

            //Transformamos a 'String' do conteúdo em Objeto JSON
            resultado = new JSONObject(conteudo);

        } catch (UnsupportedEncodingException e) {
            Log.e("ProjetoMapas", e.getMessage());
        } catch (ClientProtocolException e) {
            Log.e("ProjetoMapas", e.getMessage());
        } catch (IOException e) {
            Log.e("ProjetoMapas", e.getMessage());
        } catch (JSONException e) {
            Log.e("ProjetoMapas", e.getMessage());
        }

        //Retornamos o conteúdo em formato JSON
        return resultado;
    }

    public void pintarCaminho(JSONObject json) {
        try {
            //Recupera a lista de possíveis rotas
            JSONArray listaRotas = json.getJSONArray("routes");
            //Para efeito de aprendizado iremos utilizar apenas a primeira opção
            JSONObject rota = listaRotas.getJSONObject(0);
            //Recuperamos os pontos a serem pintados para que surga a 'linha' no mapa
            String pontosPintar = rota.getJSONObject("overview_polyline").getString("points");
            //Recuperamos a lista de latitudes e longitudes para sabermos exatamente onde pintar
            List<LatLng> listaCordenadas = extrairLatLngDaRota(pontosPintar);

            //Percorremos por cada cordenada obtida
            for(int ponto = 0; ponto < listaCordenadas.size()-1 ; ponto++){
                //Definimos o ponto atual como origem
                LatLng pontoOrigem= listaCordenadas.get(ponto);
                //Definimos o próximo ponto como destino
                LatLng pontoDestino= listaCordenadas.get(ponto + 1);
                //Criamos um objeto do tipo PolylineOption para adicionarmos os pontos de origem e destino
                PolylineOptions opcoesDaLinha = new PolylineOptions();
                //Adicionamos os pontos de origem e destino da linha que vamos traçar
                opcoesDaLinha.add(new LatLng(pontoOrigem.latitude, pontoOrigem.longitude),
                        new LatLng(pontoDestino.latitude,  pontoDestino.longitude));
                //Criamos a linha de acordo com as opções que configuramos acima e adicionamos em nosso mapa
                Polyline line = mMap.addPolyline(opcoesDaLinha);
                //Mudamos algumas propriedades da linha que acabamos de adicionar em nosso mapa
                line.setWidth(5);
                line.setColor(Color.BLUE);
                line.setGeodesic(true);
            }
        }
        catch (JSONException e) {
            Log.e("ProjetoMapas", e.getMessage());
        }
    }

    private List<LatLng> extrairLatLngDaRota(String pontosPintar) {
        List<LatLng> listaResult = new ArrayList<LatLng>();
        int index = 0, len = pontosPintar.length();
        int lat = 0, lng = 0;

        while (index < len) {

            int b, shift = 0, result = 0;
            do {
                b = pontosPintar.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = pontosPintar.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            listaResult.add(p);
        }

        return listaResult;
    }

    private class MinhaAsyncTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject resultJSON = requisicaoHTTP(params[0]);
            return resultJSON;
        }

        @Override
        protected void onPostExecute(JSONObject resultadoRequisicao) {
            super.onPostExecute(resultadoRequisicao);
            if(resultadoRequisicao != null){
                pintarCaminho(resultadoRequisicao);
            }
        }
    }

}
