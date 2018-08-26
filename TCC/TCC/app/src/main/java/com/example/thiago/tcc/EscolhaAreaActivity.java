package com.example.julieti.tcc;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.example.julieti.tcc.db.repositorios.AreaRepositorio;
import com.example.julieti.tcc.modelo.Area;

public class EscolhaAreaActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private CursorAdapter mAdapter;
    private AreaRepositorio mRepositorio;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolha_area);
        mAdapter = new AreaCursorAdapter(this, null);
        mRepositorio = new AreaRepositorio(this);
        mListView = (ListView) findViewById(R.id.lvArea);
        getSupportLoaderManager().initLoader(0, null, this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_activity_escolha_area));
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                Area area = AreaRepositorio.areaFromCursor(cursor);
                Intent it = new Intent(EscolhaAreaActivity.this, EscolhaTemaActivity.class);
                it.putExtra(EscolhaTemaActivity.EXTRA_AREA, area);
                startActivity(it);
            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return mRepositorio.buscar(this, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    public void back(View v) {
        finish();
    }
}
