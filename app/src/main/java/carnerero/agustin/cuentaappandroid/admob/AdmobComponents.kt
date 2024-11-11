package carnerero.agustin.cuentaappandroid.admob

import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun adaptiveAdSize(): AdSize {
    val context = LocalContext.current
    val density = LocalDensity.current.density

    // Obtenemos el ancho de la pantalla en píxeles
    val displayMetrics = DisplayMetrics().apply {
        context.display?.getRealMetrics(this)
    }
    val adWidthPixels = displayMetrics.widthPixels.toFloat()

    // Convertimos el ancho a dp y calculamos el AdSize adecuado
    val adWidthDp = (adWidthPixels / density).toInt()
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidthDp)
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun AdBanner() {
    val context = LocalContext.current

    // Obtén el tamaño adecuado del anuncio
    val adSize = adaptiveAdSize()

    // Crea el AdView y carga el anuncio
    AndroidView(
        factory = {
            AdView(context).apply {
                this.adUnitId = "ca-app-pub-3940256099942544~3347511713" // Reemplázalo por tu propio ID de unidad de anuncios
                this.setAdSize(adSize)

                // Listener para obtener detalles del estado del anuncio
                this.adListener = object : com.google.android.gms.ads.AdListener() {

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        Log.d("AdBanner", "Ad Loaded Successfully")
                    }
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        Log.d("AdBanner", "Failed to load ad: $p0")
                    }


                    override fun onAdOpened() {
                        super.onAdOpened()
                        Log.d("AdBanner", "Ad Opened")
                    }

                    override fun onAdClosed() {
                        super.onAdClosed()
                        Log.d("AdBanner", "Ad Closed")
                    }
                }

                // Cargar el anuncio
                val adRequest = AdRequest.Builder().build()
                this.loadAd(adRequest)
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}
