package com.example.julieti.tcc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.julieti.tcc.db.repositorios.TemaTextoRepositorio;
import com.example.julieti.tcc.modelo.Tema;

public class ApresentacaoTemaActivity extends AppCompatActivity {

    public static final String EXTRA_TEMA = "extraTema";
    private Button btNext;
    private Button btBack;
    private TextSwitcher textSwitcher;
    private String texto[];
    private int textoIndice;
    private Tema mTema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apresentacao_tema);

        Bundle bundle =  getIntent().getExtras();
        if (bundle != null) {
            mTema = bundle.getParcelable(EXTRA_TEMA);
        } else {
            mTema = new Tema(0, "Tema", 0);
        }

        btNext = (Button) findViewById(R.id.btnext_apptema);
        btBack = (Button) findViewById(R.id.btback_apptema);
        textSwitcher = (TextSwitcher) findViewById(R.id.textswitcher_apptema);
        setFactory();
        loadAnimation();

        texto = new TemaTextoRepositorio(this).textoPorTema(mTema.getId());

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
                TextView textoIntroducao = new TextView(ApresentacaoTemaActivity.this);
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
                    Intent it = new Intent(ApresentacaoTemaActivity.this, PercursosActivity.class);
                    it.putExtra(PercursosActivity.EXTRA_TEMA, mTema);
                    startActivity(it);
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
