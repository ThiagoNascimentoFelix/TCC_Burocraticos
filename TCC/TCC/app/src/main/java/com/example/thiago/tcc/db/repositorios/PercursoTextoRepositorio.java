package com.example.julieti.tcc.db.repositorios;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.julieti.tcc.db.AppSQLHelper;


public class PercursoTextoRepositorio {

    private AppSQLHelper helper;

    public PercursoTextoRepositorio(Context ctx) {
        helper = new AppSQLHelper(ctx);
    }

    public String[] textoPorPercurso(long percursoId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(AppSQLHelper.TABELA_PERCURSO_TEXTO);
        Cursor cursor = queryBuilder.query(db, new String[]{ AppSQLHelper.COL_PERCURSO_TEXTO_TEXTO },
                AppSQLHelper.COL_PERCURSO_TEXTO_PERCURSOID + " = ?",
                new String[]{ Long.toString(percursoId) }, null, null,
                AppSQLHelper.COL_PERCURSO_TEXTO_ORDEM);

        String[] textos = new String[cursor.getCount()];
        int posicao = 0;
        while (cursor.moveToNext()) {
            String texto = cursor.getString(
                    cursor.getColumnIndex(AppSQLHelper.COL_PERCURSO_TEXTO_TEXTO));
            textos[posicao++] = texto;
        }
        cursor.close();
        db.close();
        return textos;
    }

}
