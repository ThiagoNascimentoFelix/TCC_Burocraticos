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

public class IntroducaoActivity extends AppCompatActivity  {

    private Button btNext;
    private Button btBack;
    private TextSwitcher textSwitcher;
    private String texto[];
    private int textoIndice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        texto = getResources().getStringArray(R.array.introducao_textos);
        setContentView(R.layout.activity_introducao);

        btBack = (Button) findViewById(R.id.btback);
        btNext = (Button) findViewById(R.id.btnext);
        textSwitcher = (TextSwitcher) findViewById(R.id.textswitcher);
        setFactory();

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        textSwitcher.setAnimation(in);
        textSwitcher.setText(texto[textoIndice]);

        setListener();
    }

    public void back (View v){
        Intent it = new Intent(this , MainActivity.class);
        startActivity(it);
    }

    private void setFactory(){
        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textoIntroducao = new TextView(IntroducaoActivity.this);
                textoIntroducao.setTextSize(20);
                textoIntroducao.setGravity(Gravity.CENTER_HORIZONTAL);
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
                    Intent it = new Intent(IntroducaoActivity.this, EscolhaAreaActivity.class);
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
                textoAnterior();
                if (textoIndice == 0) {
                    btBack.setVisibility(View.INVISIBLE);
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
