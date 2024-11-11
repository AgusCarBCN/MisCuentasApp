package carnerero.agustin.cuentaappandroid.admob

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.window.layout.WindowMetricsCalculator
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError


@Composable
fun AdmobBanner() {
    Column {
        AndroidView(modifier = Modifier.fillMaxWidth(), factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = "ca-app-pub-3940256099942544/6300978111"
                loadAd(AdRequest.Builder().build())
                this.adListener = object : AdListener() {
                    override fun onAdClicked() {
                        Log.d(TAG, "onAdClicked: ")
                        // Code to be executed when the user clicks on an ad.
                    }

                    override fun onAdClosed() {
                        Log.d(TAG, "onAdClosed: ")
                        // Code to be executed when the user is about to return
                        // to the app after tapping on an ad.
                    }

                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.d(TAG, "onAdFailedToLoad: $adError")
                        // Code to be executed when an ad request fails.
                    }

                    override fun onAdImpression() {
                        // Code to be executed when an impression is recorded
                        // for an ad.
                    }

                    override fun onAdLoaded() {
                        Log.d(TAG, "onAdLoaded: ")
                        // Code to be executed when an ad finishes loading.
                    }

                    override fun onAdOpened() {
                        Log.d(TAG, "onAdOpened: ")
                        // Code to be executed when an ad opens an overlay that
                        // covers the screen.
                    }
                }
            }


        })

    }


}



