package com.example.julieti.tcc;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.julieti.tcc.db.repositorios.PercursoTextoRepositorio;
import com.example.julieti.tcc.modelo.Percurso;

public class BuscarInformacaoActivity extends AppCompatActivity {

    public static final String EXTRA_PERCURSO = "extraPercurso";
    private Button btNext;
    private Button btBack;
    private TextSwitcher textSwitcher;
    private String texto[];
    private int textoIndice;
    private Percurso mPercurso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_informacao);

        Bundle bundle =  getIntent().getExtras();
        if (bundle != null) {
            mPercurso = bundle.getParcelable(EXTRA_PERCURSO);
        } else {
            mPercurso = new Percurso("Percurso 1", "");
        }

        btNext = (Button) findViewById(R.id.btnext_buscarInformacao);
        btBack = (Button) findViewById(R.id.btback_buscarInformacao);
        textSwitcher = (TextSwitcher) findViewById(R.id.textswitcher_buscarInformacao);
        setFactory();
        loadAnimation();

        texto = new PercursoTextoRepositorio(this).textoPorPercurso(mPercurso.getId());

        if (texto.length > 0) {
            textSwitcher.setText(texto[textoIndice]);
        }

        setListener();
    }

    public void loadAnimation(){
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        textSwitcher.setAnimation(in);
    }

    public void setFactory(){
        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textoIntroducao = new TextView(BuscarInformacaoActivity.this);
                textoIntroducao.setTextSize(10);
                textoIntroducao.setGravity(Gravity.CENTER);
                textoIntroducao.setTextColor(Color.BLACK);
                return textoIntroducao;
            }
        });
    }

    private void setListener(){
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((textoIndice + 1) >= texto.length) {
                    finish();
                } else {
                    btBack.setVisibility(View.VISIBLE);
                    proximoTexto();
                }
            }
        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textoIndice == 0) {
                    finish();
                } else {
                    textoAnterior();
                }
            }
        });
    }

    private void proximoTexto() {
        textoIndice++;
        textSwitcher.setText(texto[textoIndice]);
    }

    private void textoAnterior() {
        textoIndice--;
        textSwitcher.setText(texto[textoIndice]);
    }

}
