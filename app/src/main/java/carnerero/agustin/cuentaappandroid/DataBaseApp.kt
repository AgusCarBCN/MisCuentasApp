package carnerero.agustin.cuentaappandroid

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseApp(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
): SQLiteOpenHelper(context, name, factory, version) {

    //Definción de sentencias para crear tablas

    private val CREATE_TABLE_USER = "CREATE TABLE USUARIO"+
                                        "(dni TEXT PRIMARY KEY,"+
                                        "nombre TEXT,"+
                                        "ciudad TEXT,"+
                                        "domicilio TEXT,"+
                                        "telefono TEXT,"+
                                        "password TEXT)"

    private val CREATE_TABLE_CUENTA = "CREATE TABLE CUENTA "+
                                        "(iban TEXT PRIMARY KEY,"+
                                        "saldo DECIMAL(10, 2),"+
                                        "dni TEXT,"+
                                        "FOREIGN KEY (dni) REFERENCES USUARIO (dni) ON UPDATE CASCADE ON DELETE CASCADE)"

    private val CREATE_TABLE_MOVIMIENTO = "CREATE TABLE MOVIMIENTO"+
                                          "(id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                                          "importe REAL,"+
                                          "descripcion TEXT,"+
                                          "iban TEXT,"+
                                          "fechaImporte DATE,"+
                                          "FOREIGN KEY (iban) REFERENCES CUENTA (iban) ON UPDATE CASCADE ON DELETE CASCADE)"

    private val CREATE_TABLE_INGRESO = "CREATE TABLE INGRESO"+
                                        "(id INTEGER PRIMARY KEY,"+
                                        "FOREIGN KEY (id) REFERENCES MOVIMIENTO (id) ON UPDATE CASCADE ON DELETE CASCADE)"

    private val CREATE_TABLE_GASTO = "CREATE TABLE GASTO"+
                                        "(id INTEGER PRIMARY KEY,"+
                                        "FOREIGN KEY (id) REFERENCES MOVIMIENTO (id) ON UPDATE CASCADE ON DELETE CASCADE)"


    private val CREATE_TRIGGER=
        "CREATE TRIGGER IF NOT EXISTS insertgastoingreso AFTER INSERT ON MOVIMIENTO\nFOR EACH ROW\nBEGIN\n    -- Verificar si el importe es mayor que 0 (ingreso) o menor que 0 (gasto)\n    INSERT INTO INGRESO (id)\n    SELECT NEW.id\n    WHERE NEW.importe > 0;\n\n    INSERT INTO GASTO (id)\n    SELECT NEW.id\n    WHERE NEW.importe < 0;\nEND;\n"


    override fun onCreate(database: SQLiteDatabase?) {


            database?.execSQL(CREATE_TABLE_USER)
            database?.execSQL(CREATE_TABLE_CUENTA)
            database?.execSQL(CREATE_TABLE_MOVIMIENTO)
            database?.execSQL(CREATE_TABLE_INGRESO)
            database?.execSQL(CREATE_TABLE_GASTO)
            database?.execSQL(CREATE_TRIGGER)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}