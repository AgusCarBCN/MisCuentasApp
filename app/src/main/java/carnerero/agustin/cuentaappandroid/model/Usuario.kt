package carnerero.agustin.cuentaappandroid.model

data class Usuario(
    val dni: String,
    val nombre: String,
    val domicilio: String,
    val ciudad: String,
    val codigoPostal: String,
    val telefono: String,
    val password: String

)

