package com.example.julieti.tcc;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.julieti.tcc.db.repositorios.TemaRepositorio;
import com.example.julieti.tcc.modelo.Area;
import com.example.julieti.tcc.modelo.Tema;

public class EscolhaTemaActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EXTRA_AREA = "extraArea";
    private Area mArea;
    private ListView mListView;
    private TemaCursorAdapter mAdapter;
    private TemaRepositorio mRepositorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolha_tema);
        Bundle bundle =  getIntent().getExtras();
        if (bundle != null) {
            mArea = bundle.getParcelable(EXTRA_AREA);
        } else {
            mArea = new Area(0, "Área");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTema);
        toolbar.setTitle(mArea.getTitulo());
        mAdapter = new TemaCursorAdapter(this, null);
        mRepositorio = new TemaRepositorio(this);
        mListView = (ListView) findViewById(R.id.lvTema);
        getSupportLoaderManager().initLoader(0, null, this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(EscolhaTemaActivity.this, ApresentacaoTemaActivity.class);
                Cursor cursor = (Cursor)parent.getItemAtPosition(position);
                Tema tema = TemaRepositorio.temaFromCursor(cursor);
                it.putExtra(ApresentacaoTemaActivity.EXTRA_TEMA, tema);
                startActivity(it);
            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return mRepositorio.buscar(this, Long.toString(mArea.getId()));
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
