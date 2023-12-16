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

    private val createUserTable = "CREATE TABLE USUARIO"+
                                        "(dni TEXT PRIMARY KEY,"+
                                        "nombre TEXT,"+
                                        "domicilio TEXT,"+
                                        "ciudad TEXT,"+
                                        "codigopostal TEXT,"+
                                        "email TEXT,"+
                                        "password TEXT)"

    private val createAccountTable = "CREATE TABLE CUENTA "+
                                        "(iban TEXT PRIMARY KEY,"+
                                        "saldo DECIMAL(10, 2),"+
                                        "dni TEXT,"+
                                        "FOREIGN KEY (dni) REFERENCES USUARIO (dni) ON UPDATE CASCADE ON DELETE CASCADE)"

    private val createMovTable = "CREATE TABLE MOVIMIENTO"+
                                          "(id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                                          "importe REAL,"+
                                          "descripcion TEXT,"+
                                          "iban TEXT,"+
                                          "fechaImporte DATE,"+
                                          "FOREIGN KEY (iban) REFERENCES CUENTA (iban) ON UPDATE CASCADE ON DELETE CASCADE)"



    override fun onCreate(database: SQLiteDatabase?) {

            database?.execSQL("PRAGMA foreign_keys = ON;")
            database?.execSQL(createUserTable)
            database?.execSQL(createAccountTable)
            database?.execSQL(createMovTable)
            //database?.execSQL(createIncomeTable)
            //database?.execSQL(createBillTable)
            //database?.execSQL(createTrigger)

    }

    override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }


}