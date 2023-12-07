package carnerero.agustin.cuentaappandroid.model

data class Usuario(
    var dni: String,
    var nombre: String,
    var domicilio: String,
    var ciudad: String,
    var codigoPostal: String,
    var email: String,
    var password: String

)

