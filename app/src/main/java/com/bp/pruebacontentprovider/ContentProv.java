package com.bp.pruebacontentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Clase que hereda de ContentProvider y contiene los principales métodos para operar en un ContentProvider.
 */

public class ContentProv extends ContentProvider {

    //URI: enlace al proveedor de contenidos.
    public static final Uri contentUri = Uri.parse("content://com.bp.pruebacontentprovider.ContentProv");
    /*MIME: permite identificar los datos devueltos.
    *   Consultas de un elemento: vnd.android.cursor.item
    *   Consultas de varios elementos: vnd.android.cursor.dir */
    private final String CONTENT_PROVIDER_MIME = "vnd.android.cursor.item/vnd.bp.pruebacontentprovider.provider.datos";

    private Helper dbHelper;

    /* Inicializa el ContentProvider creando una instancia de Helper, que hereda de SQLiteOpenHelper
    * y sirve para conectarse con la BBDD. */
    @Override
    public boolean onCreate() {
        dbHelper = new Helper(getContext(), DataBase.NOMBRE_BDD,
                null, DataBase.VERSION);
        return true; //Devuelve verdadero si se inicializa con éxito.
    }

    /* Devuelve el tipo MIME del proveedor de contenidos. */
    @Override
    public String getType(Uri uri) {
        return CONTENT_PROVIDER_MIME;
    }

    /* Permite conocer el ID del elemento consultado gracias a la URI que recibe
    * como parámetro. El ID del elemento se encuentra en la última parte de la URI. */
    public long getId(Uri contentUri) {
        String lastPathSegment = contentUri.getLastPathSegment();
        if (lastPathSegment != null) {
            return Long.parseLong(lastPathSegment); //Devuelve el valor correspondiente al ID del elemento.
        }
        /* Si no encuentra el valor del ID. Esto podría indicar que la consulta afecta a una coleccion de datos
        en lugar de a un elemento individual. */
        return -1;
    }

    /* Permite insertar un elemento en el proveedor de contenidos. Recibe la URI y los elementos
    * que se insertarán. */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //Se abre conexión en modo escritura.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            //Se insertan los elementos con insertOrThrow. Este método devuelve un valor...
            long id = db.insertOrThrow(DataBase.TABLA_DATOS, null, values);

            //... si es -1 se ha producido un error en la insercción.
            if (id == -1) {
                throw new RuntimeException(String.format(
                        "%s : Error al insertar [%s] motivos desconocidos.",
                        "AndroidProvider", values, uri));
            } else { //... si no es -1 El valor devuelto se corresponde con la URI del elemento insertado.
                return ContentUris.withAppendedId(uri, id);
            }

        } finally {
            db.close(); //Cerrar conexión.
        }
    }

    /* Permite obtener un conjunto de elementos de un ContentProvider. El número de elementos depende
    * de la URI pasada como parámetro. */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        //Primero recupera el ID de la URI.
        long id = getId(uri);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        /*Si es menor que 0 significa que la consulta se hace sobre el conjunto de elementos
        del ContentProvider. */
        if (id < 0) {
            return db
                    .query(DataBase.TABLA_DATOS,
                            projection, selection, selectionArgs, null, null, sortOrder); // Muestra el conjunto de elementos.
        } else { // De lo contrario significa que la consulta se hace sobre un elemento en particular.
            return db.query(DataBase.TABLA_DATOS,
                    projection, DataBase.COL_1 + "=" + id, null, null, null, null); //Muestra sólo el elemento que tenga ese ID.
        }
    }

    /* Permite eleminar uno o varios elementos de un ContentProvider. El funcionamiento es igual
    * que el método query. */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        long id = getId(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            if (id < 0)
                return db.delete(DataBase.TABLA_DATOS, selection, selectionArgs); //Borra el conjunto.
            else
                return db.delete(DataBase.TABLA_DATOS,
                        DataBase.COL_1 + "=" + id, selectionArgs); //Borra solo el elemento con ese ID.
        } finally {
            db.close();
        }
    }

    /* Permite actualizar uno o varios elementos de un ContentProvider. El funcionamiento es igual
    * que el método query y delete. */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        long id = getId(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            if (id < 0)
                return db.update(DataBase.TABLA_DATOS, values,
                        selection, selectionArgs); //Actualiza todos los elementos.
            else
                return db.update(DataBase.TABLA_DATOS, values,
                        DataBase.COL_1 + "=" + id, null); //Actualiza solo el elemento con ese ID.
        } finally {
            db.close();
        }
    }
}
