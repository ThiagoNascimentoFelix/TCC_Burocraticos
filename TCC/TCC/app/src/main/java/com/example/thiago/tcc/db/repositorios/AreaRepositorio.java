package com.example.julieti.tcc.db.repositorios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.julieti.tcc.db.AppSQLHelper;
import com.example.julieti.tcc.db.providers.AreaContentProvider;
import com.example.julieti.tcc.modelo.Area;

import java.util.List;



public class AreaRepositorio {
    private Context ctx;

    public AreaRepositorio(Context ctx) {
        this.ctx = ctx;
    }

    private long inserir(Area area) {
        Uri uri = ctx.getContentResolver().insert(
                AreaContentProvider.CONTENT_URI,
                getValues(area));
        long id = Long.parseLong(uri.getLastPathSegment());
        if (id != -1) {
            area.setId(id);
        }
        return id;
    }

    private int atualizar(Area area) {
        Uri uri = Uri.withAppendedPath(
                AreaContentProvider.CONTENT_URI, String.valueOf(area.getId()));
        int linhasAfetadas = ctx.getContentResolver().update(uri, getValues(area), null, null);
        return linhasAfetadas;
    }

    public void salvar(Area area) {
        if (area.getId() == 0) {
            inserir(area);
        } else {
            atualizar(area);
        }
    }

    public int excluir(Area area) {
        Uri uri = Uri.withAppendedPath(
                AreaContentProvider.CONTENT_URI, String.valueOf(area.getId()));
        int linhasAfetadas = ctx.getContentResolver().update(uri, getValues(area), null, null);
        return linhasAfetadas;
    }

    public CursorLoader buscar(Context ctx, String s) {
        String where = null;
        String[] whereArgs = null;
        if (s != null) {
            where = AppSQLHelper.COL_AREA_ID + " Like ?";
            whereArgs = new String[]{ "%" + s + "%" };
        }

        return new CursorLoader(
                ctx,
                AreaContentProvider.CONTENT_URI,
                null,
                where,
                whereArgs,
                AppSQLHelper.COL_AREA_ID);
    }

    private ContentValues getValues(Area area) {
        ContentValues cv = new ContentValues();
        cv.put(AppSQLHelper.COL_AREA_ID, area.getId());
        cv.put(AppSQLHelper.COL_AREA_TITULO, area.getTitulo());
        return cv;
    }

    public static Area areaFromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(AppSQLHelper.COL_AREA_ID));
        String titulo = cursor.getString(cursor.getColumnIndex(AppSQLHelper.COL_AREA_TITULO));
        return new Area(id, titulo);
    }
}
