package carnerero.agustin.cuentaappandroid.dao

import android.content.ContentValues
import android.database.SQLException
import carnerero.agustin.cuentaappandroid.DataBaseApp
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario

class MovimientoBancarioDAO(private val admin: DataBaseApp) {

    fun nuevoIngreso(movimientoBancario: MovimientoBancario) {
        val db = admin.writableDatabase
        val values = ContentValues()
        values.put("importe", movimientoBancario.importe)
        values.put("descripcion", movimientoBancario.descripcion)
        values.put("iban", movimientoBancario.iban)
        values.put("fechaImporte",movimientoBancario.fechaImporte)
        try {
            db.insert("MOVIMIENTO", null, values)
        } catch (e: SQLException) {

        }
        db.close()
    }
}