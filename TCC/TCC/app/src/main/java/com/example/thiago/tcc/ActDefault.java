package com.example.julieti.tcc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ActDefault extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_default);

    }

    public void back (View v){

        Intent it = new Intent(this , EscolhaAreaActivity.class);
        startActivity(it);
    }

}
