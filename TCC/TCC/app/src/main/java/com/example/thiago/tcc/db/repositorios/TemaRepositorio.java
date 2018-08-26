package com.example.julieti.tcc.db.repositorios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import com.example.julieti.tcc.db.AppSQLHelper;
import com.example.julieti.tcc.db.providers.TemaContentProvider;
import com.example.julieti.tcc.modelo.Percurso;
import com.example.julieti.tcc.modelo.Tema;


public class TemaRepositorio {
    private Context ctx;

    public TemaRepositorio(Context ctx) {
        this.ctx = ctx;
    }

    private long inserir(Tema tema) {
        Uri uri = ctx.getContentResolver().insert(
                TemaContentProvider.CONTENT_URI,
                getValues(tema));
        long id = Long.parseLong(uri.getLastPathSegment());
        if (id != -1) {
            tema.setId(id);
        }
        return id;
    }

    private int atualizar(Tema tema) {
        Uri uri = Uri.withAppendedPath(
                TemaContentProvider.CONTENT_URI, String.valueOf(tema.getId()));
        int linhasAfetadas = ctx.getContentResolver().update(uri, getValues(tema), null, null);
        return linhasAfetadas;
    }

    public void salvar(Tema tema) {
        if (tema.getId() == 0) {
            inserir(tema);
        } else {
            atualizar(tema);
        }
    }

    public int excluir(Tema tema) {
        Uri uri = Uri.withAppendedPath(
                TemaContentProvider.CONTENT_URI, String.valueOf(tema.getId()));
        int linhasAfetadas = ctx.getContentResolver().update(uri, getValues(tema), null, null);
        return linhasAfetadas;
    }

    public CursorLoader buscar(Context ctx, String s) {
        String where = null;
        String[] whereArgs = null;
        if (s != null) {
            where = AppSQLHelper.COL_TEMA_AREAID + " = ?";
            whereArgs = new String[]{ s };
        }
        return new CursorLoader(
            ctx,
            TemaContentProvider.CONTENT_URI,
            null,
            where,
            whereArgs,
            AppSQLHelper.COL_TEMA_ID);
    }

    private ContentValues getValues(Tema tema) {
        ContentValues cv = new ContentValues();
        cv.put(AppSQLHelper.COL_TEMA_ID, tema.getId());
        cv.put(AppSQLHelper.COL_TEMA_TITULO, tema.getTitulo());
        cv.put(AppSQLHelper.COL_TEMA_AREAID, tema.getAreaId());
        return cv;
    }

    public static Tema temaFromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(AppSQLHelper.COL_TEMA_ID));
        String titulo = cursor.getString(cursor.getColumnIndex(AppSQLHelper.COL_TEMA_TITULO));
        long areaId = cursor.getLong(cursor.getColumnIndex(AppSQLHelper.COL_TEMA_AREAID));
        return new Tema(id, titulo, areaId);
    }
}