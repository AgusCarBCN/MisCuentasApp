package carnerero.agustin.cuentaappandroid.model

data class Usuario(
    val dni: String,
    val nombre: String,
    var domicilio: String,
    var ciudad: String,
    var codigoPostal: String,
    var email: String,
    var password: String

)

