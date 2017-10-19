package com.bp.pruebacontentprovider;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Esta clase creará y gestionará las diferentes versiones de la BBDD.
 */

public class Helper extends SQLiteOpenHelper {

    //Nombre de la tabla.
    private static final String TABLA_DATOS = "tabla_datos";
    //Id columna 1.
    private static final String COL_1 = "DATO1";
    //Id columna 2.
    private static final String COL_2 = "DATO2";
    //Id columna 3.
    private static final String COL_3 = "DATO3";

    //Creación de la tabla.
    /* Columna 1 representa el ID, clave primaria de la tabla. Es necesario autoincrementarla para
    * que se puedan gestionar automaticamente los id´s de los registros de la tabla. */
    private static final String CREATE_BDD = "CREATE TABLE " + TABLA_DATOS + " (" +
            COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 + " TEXT NOT NULL, " +
            COL_3 + " TEXT NOT NULL); ";

    /* Permite crear un objeto que genera y gestiona una BBDD.
    *  Recibe:
    *       Contexto.
    *       Nombre.
    *       CursorFactory: Sobrecarga la clase que gestiona la creación de los Cursores, los cuáles
    *       permiten acceder al contenido de las consultas.
    *       Versión.*/
    public Helper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super (context, name, factory, version);
    }

    //Permite crear la BBDD mediante el método execSQL().
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BDD);
    }

    /*Se utiliza cuando se incrementa la versión de la BBDD. Gestiona la compatibilidad
    * con la versión anterior. */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLA_DATOS);
        onCreate(db);
    }

}
