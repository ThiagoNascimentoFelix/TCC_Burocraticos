package com.example.julieti.tcc;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.julieti.tcc.db.AppSQLHelper;

/**
 * Created by Julieti on 24/05/17.
 */

public class TemaCursorAdapter extends CursorAdapter {

    public TemaCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtTema = (TextView) view.findViewById(android.R.id.text1);
        txtTema.setText(cursor.getString(cursor.getColumnIndex(AppSQLHelper.COL_TEMA_TITULO)));
    }
}
