package com.example.julieti.tcc;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.julieti.tcc.db.repositorios.PercursoRepositorio;
import com.example.julieti.tcc.modelo.Tema;

public class PercursosActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String EXTRA_TEMA = "extraTema";
    private Tema mTema;
    private PercursoRepositorio mRepositorio;
    private PercursosCursorAdapter mAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percursos);

        Bundle bundle =  getIntent().getExtras();
        if (bundle != null) {
            mTema = bundle.getParcelable(EXTRA_TEMA);
        } else {
            mTema = new Tema(0, "Tema", 0);
        }
        mRepositorio = new PercursoRepositorio(this);
        mAdapter = new PercursosCursorAdapter(this, null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPercurso);
        toolbar.setTitle(mTema.getTitulo());

        mListView = (ListView) findViewById(R.id.lvPercursos);
        getSupportLoaderManager().initLoader(0, null, this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return mRepositorio.buscar(this, Long.toString(mTema.getId()));
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
