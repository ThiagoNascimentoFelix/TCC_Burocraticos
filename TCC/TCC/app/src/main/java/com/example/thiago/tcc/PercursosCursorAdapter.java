package com.example.julieti.tcc;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.julieti.tcc.db.repositorios.PercursoRepositorio;
import com.example.julieti.tcc.modelo.Percurso;


public class PercursosCursorAdapter extends CursorAdapter {


    public PercursosCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_percurso, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Percurso percurso = PercursoRepositorio.percursoFromCursor(cursor);
        TextView txtPercurso = (TextView) view.findViewById(R.id.txtPercurso);
        Button btnPercurso = (Button) view.findViewById(R.id.btnPercursoGo);
        switch (percurso.getStatus()) {
            case "BLOQUEADO":
                txtPercurso.setTextColor(Color.GRAY);
                break;
            case "COMPLETO":
                txtPercurso.setTextColor(Color.GREEN);
                break;
            default:
                txtPercurso.setTextColor(Color.BLUE);
        }
        txtPercurso.setText(percurso.getTitulo());
        btnPercurso.setOnClickListener(new PercursoButtonListener(percurso));
    }

    private class PercursoButtonListener implements View.OnClickListener {
        private Percurso percurso;

        public PercursoButtonListener(Percurso percurso) {
            this.percurso = percurso;
        }

        @Override
        public void onClick(View view) {
            if (percurso.getStatus().equals("BLOQUEADO")) {
                Toast.makeText(view.getContext(),"É necessário completar o percurso anterior para prosseguir!",Toast.LENGTH_LONG).show();
            }else{
                Intent it = new Intent(view.getContext(), MapaActivity.class);
                it.putExtra(MapaActivity.EXTRA_PERCURSO, percurso);
                view.getContext().startActivity(it);
            }
        }

    }
}
