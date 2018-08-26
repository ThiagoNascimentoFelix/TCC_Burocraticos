package com.example.julieti.tcc.db.repositorios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import com.example.julieti.tcc.db.AppSQLHelper;
import com.example.julieti.tcc.db.providers.PercursoContentProvider;
import com.example.julieti.tcc.modelo.Percurso;


public class PercursoRepositorio {
    private Context ctx;

    public PercursoRepositorio(Context ctx) {
        this.ctx = ctx;
    }

    private long inserir(Percurso percurso) {
        Uri uri = ctx.getContentResolver().insert(
                PercursoContentProvider.CONTENT_URI,
                getValues(percurso));
        long id = Long.parseLong(uri.getLastPathSegment());
        if (id != -1) {
            percurso.setId(id);
        }
        return id;
    }

    private int atualizar(Percurso percurso) {
        Uri uri = Uri.withAppendedPath(
                PercursoContentProvider.CONTENT_URI, String.valueOf(percurso.getId()));
        int linhasAfetadas = ctx.getContentResolver().update(uri,
                getValues(percurso), null, null);
        return linhasAfetadas;
    }

    public void salvar(Percurso percurso) {
        if (percurso.getId() == 0) {
            inserir(percurso);
        } else {
            atualizar(percurso);
        }
    }

    public int excluir(Percurso percurso) {
        Uri uri = Uri.withAppendedPath(
                PercursoContentProvider.CONTENT_URI, String.valueOf(percurso.getId()));
        int linhasAfetadas = ctx.getContentResolver().update(uri,
                getValues(percurso), null, null);
        return linhasAfetadas;
    }

    public CursorLoader buscar(Context ctx, String s) {
        String where = null;
        String[] whereArgs = null;
        if (s != null) {
            where = AppSQLHelper.COL_PERCURSO_TEMAID + " = ?";
            whereArgs = new String[]{ s };
        }
        return new CursorLoader(
            ctx,
            PercursoContentProvider.CONTENT_URI,
            null,
            where,
            whereArgs,
            AppSQLHelper.COL_AREA_TITULO);
    }

    public Percurso buscarPercursoPorId(long id) {
        AppSQLHelper helper = new AppSQLHelper(ctx);
        SQLiteDatabase db = helper.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(AppSQLHelper.TABELA_PERCURSO);
        Cursor cursor = queryBuilder.query(db,
                null,
                AppSQLHelper.COL_PERCURSO_ID + " = ?",
                new String[]{ Long.toString(id) },
                null,
                null,
                null
        );
        Percurso percurso = null;
        if(cursor.moveToNext()) {
            percurso = percursoFromCursor(cursor);
        } else {
            percurso = new Percurso(0, "Percurso", "");
        }
        cursor.close();
        db.close();
        helper.close();
        return percurso;
    }

    private ContentValues getValues(Percurso percurso) {
        ContentValues cv = new ContentValues();
        cv.put(AppSQLHelper.COL_PERCURSO_ID, percurso.getId());
        cv.put(AppSQLHelper.COL_PERCURSO_TITULO, percurso.getTitulo());
        cv.put(AppSQLHelper.COL_PERCURSO_LAT, percurso.getLatitude());
        cv.put(AppSQLHelper.COL_PERCURSO_LNG, percurso.getLongitude());
        cv.put(AppSQLHelper.COL_PERCURSO_STATUS, percurso.getStatus());
        cv.put(AppSQLHelper.COL_PERCURSO_TEMAID, percurso.getTemaId());
        return cv;
    }

    public static Percurso percursoFromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(AppSQLHelper.COL_PERCURSO_ID));
        long temaId = cursor.getLong(cursor.getColumnIndex(AppSQLHelper.COL_PERCURSO_TEMAID));
        String titulo = cursor.getString(cursor.getColumnIndex(AppSQLHelper.COL_PERCURSO_TITULO));
        float latitude = cursor.getFloat(cursor.getColumnIndex(AppSQLHelper.COL_PERCURSO_LAT));
        float longitude = cursor.getFloat(cursor.getColumnIndex(AppSQLHelper.COL_PERCURSO_LNG));
        String status = cursor.getString(cursor.getColumnIndex(AppSQLHelper.COL_PERCURSO_STATUS));
        Percurso percurso = new Percurso(id, titulo, status);
        percurso.setLatitude(latitude);
        percurso.setLongitude(longitude);
        percurso.setTemaId(temaId);
        return percurso;
    }
}
