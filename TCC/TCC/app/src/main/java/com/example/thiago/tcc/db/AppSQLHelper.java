package com.example.julieti.tcc.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class AppSQLHelper extends SQLiteOpenHelper {
    public static final String NOME_BANCO = "dbTcc";
    public static final int VERSAO_BANCO = 1;

    public static final String TABELA_AREA = "area";
    public static final String COL_AREA_ID = "_id";
    public static final String COL_AREA_TITULO = "titulo";

    public static final String TABELA_TEMA = "tema";
    public static final String COL_TEMA_ID = "_id";
    public static final String COL_TEMA_TITULO = "titulo";
    public static final String COL_TEMA_AREAID = "area_id";

    public static final String TABELA_TEMA_TEXTO = "tema_texto";
    public static final String COL_TEMA_TEXTO_ID = "_id";
    public static final String COL_TEMA_TEXTO_TEMAID = "tema_id";
    public static final String COL_TEMA_TEXTO_TEXTO = "texto";
    public static final String COL_TEMA_TEXTO_ORDEM = "ordem";

    public static final String TABELA_PERCURSO = "percurso";
    public static final String COL_PERCURSO_ID = "_id";
    public static final String COL_PERCURSO_TITULO = "titulo";
    public static final String COL_PERCURSO_STATUS = "status";
    public static final String COL_PERCURSO_LAT = "latitude";
    public static final String COL_PERCURSO_LNG = "longitude";
    public static final String COL_PERCURSO_TEMAID = "tema_id";

    public static final String TABELA_PERCURSO_TEXTO = "percurso_texto";
    public static final String COL_PERCURSO_TEXTO_ID = "_id";
    public static final String COL_PERCURSO_TEXTO_PERCURSOID = "percurso_id";
    public static final String COL_PERCURSO_TEXTO_TEXTO = "texto";
    public static final String COL_PERCURSO_TEXTO_ORDEM = "ordem";


    public AppSQLHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABELA_AREA + " (" +
                COL_AREA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_AREA_TITULO + " VARCHAR(50) NOT NULL)");
        db.execSQL("CREATE TABLE " + TABELA_TEMA + " (" +
                COL_TEMA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TEMA_TITULO + " VARCHAR(50) NOT NULL," +
                COL_TEMA_AREAID + " INTEGER NOT NULL," +
                "FOREIGN KEY(" + COL_TEMA_AREAID + ") REFERENCES " +
                TABELA_AREA + "(" + COL_AREA_ID + "))");

        db.execSQL("CREATE TABLE " + TABELA_TEMA_TEXTO + " (" +
                COL_TEMA_TEXTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TEMA_TEXTO_TEXTO + " TEXT NOT NULL," +
                COL_TEMA_TEXTO_ORDEM + " INTEGER NOT NULL," +
                COL_TEMA_TEXTO_TEMAID + " INTEGER NOT NULL," +
                "FOREIGN KEY(" + COL_TEMA_TEXTO_TEMAID + ") REFERENCES " +
                TABELA_TEMA + "(" + COL_TEMA_ID + "))");

        db.execSQL("CREATE TABLE " + TABELA_PERCURSO + " (" +
                COL_PERCURSO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PERCURSO_TITULO + " VARCHAR(50) NOT NULL, " +
                COL_PERCURSO_STATUS + " VARCHAR(15) NOT NULL," +
                COL_PERCURSO_LAT + " REAL NOT NULL," +
                COL_PERCURSO_LNG + " REAL NOT NULL," +
                COL_PERCURSO_TEMAID + " INTEGER NOT NULL," +
                "FOREIGN KEY(" + COL_PERCURSO_TEMAID + ") REFERENCES " +
                TABELA_TEMA + "(" + COL_TEMA_ID + "))");

        db.execSQL("CREATE TABLE " + TABELA_PERCURSO_TEXTO + " (" +
                COL_PERCURSO_TEXTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PERCURSO_TEXTO_TEXTO + " TEXT NOT NULL," +
                COL_PERCURSO_TEXTO_ORDEM + " INTEGER NOT NULL," +
                COL_PERCURSO_TEXTO_PERCURSOID + " INTEGER NOT NULL," +
                "FOREIGN KEY(" + COL_PERCURSO_TEXTO_PERCURSOID + ") REFERENCES " +
                TABELA_PERCURSO + "(" + COL_PERCURSO_ID + "))");

        long areaId, temaId, percursoId;
        ContentValues areaValues = new ContentValues();
        ContentValues temaValues = new ContentValues();
        ContentValues temaTextoValues = new ContentValues();
        ContentValues percursoValues = new ContentValues();
        ContentValues percursoTextoValues = new ContentValues();

        areaValues.put(COL_AREA_TITULO, "Investimento");
        areaId = db.insert(TABELA_AREA, null, areaValues);

        temaValues.put(COL_TEMA_TITULO, "Abertura de Empresa");
        temaValues.put(COL_TEMA_AREAID, Long.toString(areaId));
        temaId = db.insert(TABELA_TEMA, null, temaValues);

        String textoTema1 = "Olá!";
        String textoTema2 = "Neste exemplo você irá aprender os passos para a abertura de uma empresa.";
        String textoTema3 = "Na próxima tela você terá os percursos que terão que ser percorridos obrigatóriamente para completar este" +
                " tema.";
        String textoTema4 = "Antes de abrir sua empresa, é preciso se informar.";
        String textoTema5 = "Faça uma pesquisa antecipada sobre a existência de empresas constituídas " +
                "com nomes empresariais idênticos ou semelhantes ao nome pesquisado.";
        String textoTema6 = "Essa é uma etapa obrigatória, que deve ser preenchida no site da junta comercial.";
        String textoTema7 = "Além da consulta de viabilidade do nome empresarial, ";
        String textoTema8 = "é importante que nesta etapa você procure a prefeitura onde sua empresa será instalada ";
        String textoTema9 = "para verificar os critérios de concessão do Alvará de Funcionamento para o exercício" +
                " da sua atividade no local escolhido.";
        String textoTema10 = "então .. vamos ao primeiro lugar, ou seja, a prefeitura municipal!";

        temaTextoValues.put(COL_TEMA_TEXTO_TEXTO, textoTema1);
        temaTextoValues.put(COL_TEMA_TEXTO_ORDEM, "1");
        temaTextoValues.put(COL_TEMA_TEXTO_TEMAID, Long.toString(temaId));
        db.insert(TABELA_TEMA_TEXTO, null, temaTextoValues);
        temaTextoValues.put(COL_TEMA_TEXTO_TEXTO, textoTema2);
        temaTextoValues.put(COL_TEMA_TEXTO_ORDEM, "2");
        temaTextoValues.put(COL_TEMA_TEXTO_TEMAID, Long.toString(temaId));
        db.insert(TABELA_TEMA_TEXTO, null, temaTextoValues);
        temaTextoValues.put(COL_TEMA_TEXTO_TEXTO, textoTema3);
        temaTextoValues.put(COL_TEMA_TEXTO_ORDEM, "3");
        temaTextoValues.put(COL_TEMA_TEXTO_TEMAID, Long.toString(temaId));
        db.insert(TABELA_TEMA_TEXTO, null, temaTextoValues);
		temaTextoValues.put(COL_TEMA_TEXTO_TEXTO, textoTema4);
        temaTextoValues.put(COL_TEMA_TEXTO_ORDEM, "4");
        temaTextoValues.put(COL_TEMA_TEXTO_TEMAID, Long.toString(temaId));
        db.insert(TABELA_TEMA_TEXTO, null, temaTextoValues);
		temaTextoValues.put(COL_TEMA_TEXTO_TEXTO, textoTema5);
        temaTextoValues.put(COL_TEMA_TEXTO_ORDEM, "5");
        temaTextoValues.put(COL_TEMA_TEXTO_TEMAID, Long.toString(temaId));
        db.insert(TABELA_TEMA_TEXTO, null, temaTextoValues);
		temaTextoValues.put(COL_TEMA_TEXTO_TEXTO, textoTema6);
        temaTextoValues.put(COL_TEMA_TEXTO_ORDEM, "6");
        temaTextoValues.put(COL_TEMA_TEXTO_TEMAID, Long.toString(temaId));
        db.insert(TABELA_TEMA_TEXTO, null, temaTextoValues);
		temaTextoValues.put(COL_TEMA_TEXTO_TEXTO, textoTema7);
        temaTextoValues.put(COL_TEMA_TEXTO_ORDEM, "7");
        temaTextoValues.put(COL_TEMA_TEXTO_TEMAID, Long.toString(temaId));
        db.insert(TABELA_TEMA_TEXTO, null, temaTextoValues);
		temaTextoValues.put(COL_TEMA_TEXTO_TEXTO, textoTema8);
        temaTextoValues.put(COL_TEMA_TEXTO_ORDEM, "8");
        temaTextoValues.put(COL_TEMA_TEXTO_TEMAID, Long.toString(temaId));
        db.insert(TABELA_TEMA_TEXTO, null, temaTextoValues);
		temaTextoValues.put(COL_TEMA_TEXTO_TEXTO, textoTema9);
        temaTextoValues.put(COL_TEMA_TEXTO_ORDEM, "9");
        temaTextoValues.put(COL_TEMA_TEXTO_TEMAID, Long.toString(temaId));
        db.insert(TABELA_TEMA_TEXTO, null, temaTextoValues);
		temaTextoValues.put(COL_TEMA_TEXTO_TEXTO, textoTema10);
        temaTextoValues.put(COL_TEMA_TEXTO_ORDEM, "10");
        temaTextoValues.put(COL_TEMA_TEXTO_TEMAID, Long.toString(temaId));
        db.insert(TABELA_TEMA_TEXTO, null, temaTextoValues);

        percursoValues.put(COL_PERCURSO_TITULO, "1 - Prefeitura Municipal");
        percursoValues.put(COL_PERCURSO_STATUS, "INCOMPLETO");
        percursoValues.put(COL_PERCURSO_LAT, "-31.770267");
        percursoValues.put(COL_PERCURSO_LNG, "-52.342309");
        percursoValues.put(COL_PERCURSO_TEMAID, temaId);
        percursoId = db.insert(TABELA_PERCURSO, null, percursoValues);
		
		String textoPercurso1 = "Aqui na prefeitura você obterá o Alvará.";
		String textoPercurso2 = "Após esta etapa, você precisa ir na Junta Comercial ou Cartório de Registro de Pessoa Jurídica.";
		
		
        percursoTextoValues.put(COL_PERCURSO_TEXTO_ORDEM, "1");
        percursoTextoValues.put(COL_PERCURSO_TEXTO_PERCURSOID, Long.toString(percursoId));
        percursoTextoValues.put(COL_PERCURSO_TEXTO_TEXTO, textoPercurso1);
        db.insert(TABELA_PERCURSO_TEXTO, null, percursoTextoValues);

        percursoTextoValues.put(COL_PERCURSO_TEXTO_ORDEM, "2");
        percursoTextoValues.put(COL_PERCURSO_TEXTO_PERCURSOID, Long.toString(percursoId));
        percursoTextoValues.put(COL_PERCURSO_TEXTO_TEXTO, textoPercurso2);
        db.insert(TABELA_PERCURSO_TEXTO,null,percursoTextoValues);

        percursoValues.put(COL_PERCURSO_TITULO, "2 - Cartório");
        percursoValues.put(COL_PERCURSO_STATUS, "BLOQUEADO");
        percursoValues.put(COL_PERCURSO_LAT, "-31.768116");  
        percursoValues.put(COL_PERCURSO_LNG, "-52.340751");
        percursoValues.put(COL_PERCURSO_TEMAID, temaId);
        db.insert(TABELA_PERCURSO, null, percursoValues);

        percursoValues.put(COL_PERCURSO_TITULO, "3 - Secretaria Estadual da Fazenda");
        percursoValues.put(COL_PERCURSO_STATUS, "BLOQUEADO");
        percursoValues.put(COL_PERCURSO_LAT, "-31.767042");
        percursoValues.put(COL_PERCURSO_LNG, "-52.341171"); 
        percursoValues.put(COL_PERCURSO_TEMAID, temaId);
        db.insert(TABELA_PERCURSO, null, percursoValues);


        temaValues.put(COL_TEMA_TITULO, "Investir na Bolsa de Valores");
        temaValues.put(COL_TEMA_AREAID, Long.toString(areaId));
        temaId = db.insert(TABELA_TEMA, null, temaValues);


        areaValues.put(COL_AREA_TITULO, "Código e Desefa do Consumidor");
        areaId = db.insert(TABELA_AREA, null, areaValues);


        temaValues.put(COL_TEMA_TITULO, "Tema 1 da Área 2");
        temaValues.put(COL_TEMA_AREAID, Long.toString(areaId));
        temaId = db.insert(TABELA_TEMA, null, temaValues);


/*        areaValues.put(COL_AREA_TITULO, "Área 3");
        areaId = db.insert(TABELA_AREA, null, areaValues);
        areaValues.put(COL_AREA_TITULO, "Área 4");
        areaId = db.insert(TABELA_AREA, null, areaValues);
        areaValues.put(COL_AREA_TITULO, "Área 5");
        areaId = db.insert(TABELA_AREA, null, areaValues);
*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

