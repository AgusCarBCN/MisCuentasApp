package carnerero.agustin.cuentaappandroid.model

import java.time.LocalDate

data class MovimientoBancario(
    val iban: String,
    val descripcion: String,
    val importe: Double,
    val fechaimporte: LocalDate = LocalDate.now() // Utiliza LocalDate para la fecha actual
)
