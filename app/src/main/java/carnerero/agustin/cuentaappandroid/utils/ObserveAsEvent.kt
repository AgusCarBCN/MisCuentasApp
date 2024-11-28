package carnerero.agustin.cuentaappandroid.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

// Definición de la función ObserveAsEvents.
// T es un tipo genérico que permite usar esta función con diferentes tipos de datos.
@Composable
fun <T> ObserveAsEvents(
    flow: Flow<T>, // Un flujo (Flow) de eventos que queremos observar.
    key1: Any? = null, // Clave opcional 1 para reiniciar el efecto si cambia.
    key2: Any? = null, // Clave opcional 2 para reiniciar el efecto si cambia.
    onEvent: (T) -> Unit // Función que se llamará con el evento recibido.
) {
    // Obtiene el ciclo de vida actual del dueño (la actividad o fragmento).
    val lifecycleOwner = LocalLifecycleOwner.current

    // Inicia un efecto que se ejecutará cada vez que el ciclo de vida o las claves cambien.
    LaunchedEffect(lifecycleOwner.lifecycle, key1, key2, flow) {
        // Espera hasta que el ciclo de vida esté en el estado STARTED.
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            // Cambia a un contexto de trabajo en el hilo principal.
            withContext(Dispatchers.Main.immediate) {
                // Observa y recolecta eventos del flujo. Cuando recibe un evento, llama a onEvent.
                flow.collect(onEvent)
            }
        }
    }
}
