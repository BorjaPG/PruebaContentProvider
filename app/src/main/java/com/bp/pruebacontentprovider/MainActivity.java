package com.bp.pruebacontentprovider;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

//LoaderManager.LoaderCallbacks<Cursor> Permite recuperar los datos almacenados en un ContentProvider.
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int URL_LOADER = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        insertRecords();
        /* initLoader inicia la tarea que permite recuperar los datos. Recibe:
        *   Id del Loader.
        *   Dato.
        *   CallBack: Permite recuperar los datos al final del procesamiento. */
        getLoaderManager().initLoader(URL_LOADER, null, this);
    }

    /* Instancia del loader que carga el ID pasado como parámetro. */
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case URL_LOADER: //Para el loader seleccionado
                String columns[] = new String[]{DataBase.COL_1,
                        DataBase.COL_2, DataBase.COL_3};
                return new CursorLoader(
                        this, //Conexto
                        ContentProv.contentUri, //Tabla a consultar
                        columns, //Columna a recuperar.
                        null, //Claúsula de recuperación.
                        null, //Argumento de selección.
                        null  //Orden.
                );
            default:
                //Si el identificador no es correcto.
                return null;
        }
    }

    /* Se invoca cuando el loader creado se reinicializa. De esta forma los datos anteriores
    * quedan inaccesibles. */
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    /* Se invoca cuando la carga de los datos finaliza. Los datos se almacenan en la variable
    data. */
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            String name = null;
            do {
                //Recupera los datos correspondientes mediante el nombre de la columna.
                name = cursor.getString(cursor.getColumnIndex(DataBase.COL_1))
                        + " "
                        + cursor.getString(cursor
                        .getColumnIndex(DataBase.COL_2))
                        + " "
                        + cursor.getString(cursor
                        .getColumnIndex(DataBase.COL_3));
                //Muestra el resultado en forma de Toast. Podría utilizarse un recycler u otra cosa.
                Toast.makeText(this, name + " ", Toast.LENGTH_LONG).show();
            } while (cursor.moveToNext()); //Recorre los datos recuperados mediante moveToNext().
        }
    }

    /* Permite insertar datos en el ContentProvider. */
    private void insertRecords() {

        //Los datos se almacenarán en un objeto de tipo ContentValues.
        ContentValues contact = new ContentValues();

        //Se especifica los valores que se asociarán a las columnas del ContentProvider.
        contact.put(DataBase.COL_2, "Capítulo 1");
        contact.put(DataBase.COL_3, "Presentación de la plataforma Android");

        /*Con insert de getContentResolver se insertan los datos. getContentResolver permite acceder a métodos
        * para interactuar con un ContentProvider. */
        getContentResolver()
                .insert(ContentProv.contentUri, contact); //contentUri hace referencia al ContentProvider creado.

        contact.clear();
        contact.put(DataBase.COL_2, "Capítulo 2");
        contact.put(DataBase.COL_3, "Entorno de desarrollo");
        getContentResolver()
                .insert(ContentProv.contentUri, contact);

        contact.clear();
        contact.put(DataBase.COL_2, "Capítulo 3");
        contact.put(DataBase.COL_3, "Principios de programación en Android");
        getContentResolver()
                .insert(ContentProv.contentUri, contact);
    }

}
