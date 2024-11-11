package carnerero.agustin.cuentaappandroid

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

// Clase de datos que representa un evento de SnackBar.
data class SnackBarEvent(
    val message: String, // Mensaje que se mostrará en el SnackBar.
    val action: SnackBarAction? = null // Acción opcional asociada al evento.
)

// Clase de datos que representa una acción que puede ser realizada en el SnackBar.
data class SnackBarAction(
    val name: String, // Nombre de la acción (por ejemplo, "Deshacer").
    val action: () -> Unit // Función que se ejecutará al realizar la acción.
)

// Objeto singleton que controla los eventos de SnackBar.
object SnackBarController {

    // Canal privado para enviar eventos de SnackBar.
    private val _events = Channel<SnackBarEvent>()

    // Flujo público que permite recibir los eventos enviados a través del canal.
    val events = _events.receiveAsFlow()

    // Método suspendido para enviar un evento al canal.
    suspend fun sendEvent(event: SnackBarEvent) {
        _events.send(event) // Envía el evento al canal.
    }
}
