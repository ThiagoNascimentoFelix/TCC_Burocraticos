package com.example.julieti.tcc.db.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.julieti.tcc.db.AppSQLHelper;

public class TemaContentProvider extends ContentProvider {
    private static final String AUTHORITY = "felix.thiago.tcc.temas";
    private static final String PATH = "temas";
    private static final UriMatcher S_URI_MATCHER;
    private static final int TIPO_GERAL = 1;
    private static final int TIPO_TEMA_ESPECIFICO = 2;
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
    static {
        S_URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        S_URI_MATCHER.addURI(AUTHORITY, PATH, TIPO_GERAL);
        S_URI_MATCHER.addURI(AUTHORITY, PATH + "/#", TIPO_TEMA_ESPECIFICO);
    }

    private AppSQLHelper mHelper;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = S_URI_MATCHER.match(uri);
        SQLiteDatabase sqlDb = mHelper.getWritableDatabase();

        int rowsDeleted;
        switch (uriType) {
            case TIPO_GERAL:
                rowsDeleted = sqlDb.delete(AppSQLHelper.TABELA_TEMA,
                        selection, selectionArgs);
                break;
            case TIPO_TEMA_ESPECIFICO:
                String id = uri.getLastPathSegment();
                rowsDeleted = sqlDb.delete(AppSQLHelper.TABELA_TEMA,
                        AppSQLHelper.COL_TEMA_ID + " = ?",
                        new String[]{ id });
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        int uriType = S_URI_MATCHER.match(uri);
        switch (uriType) {
            case TIPO_GERAL:
                return "vnd.android.cursor.dir/" + AUTHORITY;
            case TIPO_TEMA_ESPECIFICO:
                return "vnd.android.cursor.item/" + AUTHORITY;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = S_URI_MATCHER.match(uri);
        SQLiteDatabase sqlDb = mHelper.getWritableDatabase();
        long id;
        switch (uriType) {
            case TIPO_GERAL:
                id = sqlDb.insertWithOnConflict(AppSQLHelper.TABELA_TEMA,
                        null, values, SQLiteDatabase.CONFLICT_REPLACE);
                break;
            default:
                throw new IllegalArgumentException("URI não Suportada: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.withAppendedPath(CONTENT_URI, String.valueOf(id));
    }

    @Override
    public boolean onCreate() {
        mHelper = new AppSQLHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int uriType = S_URI_MATCHER.match(uri);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(AppSQLHelper.TABELA_TEMA);
        Cursor cursor;
        switch (uriType) {
            case TIPO_GERAL:
                cursor = queryBuilder.query(db, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case TIPO_TEMA_ESPECIFICO:
                cursor = queryBuilder.query(db, projection, selection,
                        new String[] { uri.getLastPathSegment() }, null, null, null);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = S_URI_MATCHER.match(uri);
        SQLiteDatabase sqlDb = mHelper.getWritableDatabase();
        int linhasAfetadas;
        switch (uriType) {
            case TIPO_GERAL:
                linhasAfetadas = sqlDb.update(AppSQLHelper.TABELA_TEMA,
                        values, selection, selectionArgs);
                break;
            case TIPO_TEMA_ESPECIFICO:
                String id = uri.getLastPathSegment();
                linhasAfetadas = sqlDb.update(AppSQLHelper.TABELA_TEMA,
                        values, AppSQLHelper.COL_TEMA_ID + " = ?",
                        new String[]{ id });
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return linhasAfetadas;
    }
}
