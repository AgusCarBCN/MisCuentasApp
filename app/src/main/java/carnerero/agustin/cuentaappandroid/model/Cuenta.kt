package carnerero.agustin.cuentaappandroid.model


data class Cuenta(
    val iban: String,
    var saldo: Double,
    val dni: String
)

