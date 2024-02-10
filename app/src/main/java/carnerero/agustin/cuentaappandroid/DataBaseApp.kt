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



    private val createAccountTable = "CREATE TABLE CUENTA "+
                                        "(nombreCuenta TEXT PRIMARY KEY,"+
                                        "saldo DECIMAL(10, 2))"



    private val createMovTable = "CREATE TABLE MOVIMIENTO"+
                                          "(id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                                          "importe REAL,"+
                                          "descripcion TEXT,"+
                                          "nombreCuenta TEXT,"+
                                          "fechaImporte DATE,"+
                                          "FOREIGN KEY (nombreCuenta) REFERENCES CUENTA (nombreCuenta) ON UPDATE CASCADE ON DELETE CASCADE)"



    override fun onCreate(database: SQLiteDatabase?) {

            database?.execSQL("PRAGMA foreign_keys = ON;")
            database?.execSQL(createAccountTable)
            database?.execSQL(createMovTable)
                }

    override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }


}