package carnerero.agustin.cuentaappandroid.model

import java.text.SimpleDateFormat
import java.util.Date

data class MovimientoBancario(

    val importe: Double,
    val descripcion: String,
    val iban: String,
    val fechaImporte: String = SimpleDateFormat("dd/MM/yyyy").format(Date())
)
