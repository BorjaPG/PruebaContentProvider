package com.bp.pruebacontentprovider;

/**
 * Clase modelo que representa los datos de las columnas que tendrá la BBDD.
 */

public class Modelo {

    private int col1;
    private String col2;
    private String col3;

    //Constructor vacío obligatorio.
    public Modelo() {}

    public Modelo(String dato2, String dato3) {
        this.col2 = dato2;
        this.col3 = dato3;
    }

    public int getCol1() {
        return col1;
    }

    public void setCol1(int dato1) {
        this.col1 = dato1;
    }

    public String getCol2() {
        return col2;
    }

    public void setCol2(String dato2) {
        this.col2 = dato2;
    }

    public String getCol3() {
        return col3;
    }

    public void setCol3(String dato3) {
        this.col3 = dato3;
    }

    /* Sirve para mostrar el contenido de las columnas. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre de la columna 2 = " + col2 + "\n" + "Descripción de la columna 3 = " + col3);
        return sb.toString();
    }
}
