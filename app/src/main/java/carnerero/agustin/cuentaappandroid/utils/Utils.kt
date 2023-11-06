package carnerero.agustin.cuentaappandroid.utils

import android.content.Context
import android.media.MediaPlayer
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Utils {

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        private var mediaPlayer: MediaPlayer? = null
        fun calcularImporteMes(month: Int, year: Int, importes: ArrayList<MovimientoBancario>): Float {
            var importeTotal = 0.0

            for (mov in importes) {
                var fechaImporteDate = LocalDate.parse(mov.fechaImporte, formatter)
                if (fechaImporteDate.monthValue == month && fechaImporteDate.year == year) {
                    importeTotal += mov.importe
                }
            }
            return importeTotal.toFloat()
        }
        fun sound(contex : Context){
                mediaPlayer = MediaPlayer.create(contex, R.raw.clicksound)
                mediaPlayer?.start()
        }
        fun releaseSound(){
            if (mediaPlayer != null) {
                mediaPlayer?.stop(); // Detener la reproducción si está en curso
                mediaPlayer?.release(); // Liberar los recursos del MediaPlayer
                mediaPlayer = null; // Establecer el objeto MediaPlayer en nulo
            }
        }

    }


}