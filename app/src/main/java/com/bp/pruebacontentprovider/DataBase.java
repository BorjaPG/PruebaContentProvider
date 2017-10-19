package com.bp.pruebacontentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Esta clase contiene los métodos para interactuar con la BBDD.
 */

public class DataBase {

    //Version de BBDD.
    public static final int VERSION = 2;
    //Nombre de la BBDD.
    public static final String NOMBRE_BDD = "datos.db";
    //Nombre de la tabla.
    public static final String TABLA_DATOS = "tabla_datos";
    //Nombre de cada columna.
    public static final String COL_1 = "DATO1";
    private static final int NUM_COL_1 = 0;
    public static final String COL_2 = "DATO2";
    private static final int NUM_COL_2 = 1;
    public static final String COL_3 = "DATO3";
    private static final int NUM_COL_3 = 2;

    //Variable que representa la BBDD.
    private SQLiteDatabase bdd;

    //Variable que representa la clase Helper.
    private Helper datos;

    //Permite inicializar la instancia de la clase Helper.
    public DataBase(Context context) {
        datos = new Helper(context, NOMBRE_BDD, null, VERSION);
    }

    //Permite abrir conexión con la BBDD en modo escritura.
    public void openForWrite() {
        bdd = datos.getWritableDatabase();
    }

    //Permite abrir conexión con la BBDD en modo lectura.
    public void openForRead() {
        bdd = datos.getReadableDatabase();
    }

    //Permite cerrar la conexion a la BBDD.
    public void close() {
        bdd.close();
    }

    //Permite recuperar la instancia de la BBDD.
    public SQLiteDatabase getBdd() {
        return bdd;
    }

    /* Permite insertar un dato en la tabla. Para insertar datos es necesario ContentValues,
    * que contiene una lista de claves/valor que representan el id de la columna y el valor
    * que se insertará.*/
    public long insertData(Modelo modelo) {
        ContentValues content = new ContentValues();
        content.put(COL_2, modelo.getCol2()); //Inserta en la columna 2 el objeto recibido.
        content.put(COL_3, modelo.getCol3());
        return bdd.insert(TABLA_DATOS, null, content); //Inserta en la tabla indicada los valores.
    }

    /* Permite actualizar un dato en la tabla. */
    public int updateData(int id, Modelo modelo) {
        ContentValues content = new ContentValues();
        content.put(COL_2, modelo.getCol2());
        content.put(COL_3, modelo.getCol3());
        return bdd.update(TABLA_DATOS, content, COL_1 + " = " + id, null); //Utiliza update para actualizar.
    }

    /* Permite borrar un dato de la tabla a partir de su nombre. */
    public int removeData(String name) {
        return bdd.delete(TABLA_DATOS, COL_2 + " = " + name, null); //Utiliza delete para borrar.
    }

    /* Permite obtener un dato de la tabla a partir de su nombre. Transformará el cursor en una
    * instancia de la clase Modelo. */
    public Modelo getData(String name) {
        Cursor c = bdd.query(TABLA_DATOS, new String[] { COL_1, COL_2,
                        COL_3 }, COL_2 + " LIKE \"" + name + "\"", null, null,
                null, COL_2);
        return cursorToData(c);
    }

    /* Permite transformar un cursor en un objeto de tipo Modelo. */
    public Modelo cursorToData(Cursor c) {
        if (c.getCount() == 0) {
            c.close();
            return null;
        }
        Modelo modelo = new Modelo();
        modelo.setCol1(c.getInt(NUM_COL_1));
        modelo.setCol2(c.getString(NUM_COL_2));
        modelo.setCol3(c.getString(NUM_COL_3));
        c.close();
        return modelo;
    }

    /* Permite obtener todos los datos almacenados en una tabla. */
    public ArrayList<Modelo> getAllData() {
        Cursor c = bdd.query(TABLA_DATOS, new String[] { COL_1, COL_2,
                COL_3 }, null, null, null, null, COL_2);
        if (c.getCount() == 0) {
            c.close();
            return null;
        }
        ArrayList<Modelo> dataList = new ArrayList<Modelo> ();
        while (c.moveToNext()) {
            Modelo modelo = new Modelo();
            modelo.setCol1(c.getInt(NUM_COL_1));
            modelo.setCol2(c.getString(NUM_COL_2));
            modelo.setCol3(c.getString(NUM_COL_3));
            dataList.add(modelo);
        }
        c.close();
        return dataList;
    }

}
