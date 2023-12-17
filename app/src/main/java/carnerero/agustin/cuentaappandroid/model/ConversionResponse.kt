package carnerero.agustin.cuentaappandroid.model

import com.google.gson.annotations.SerializedName

data class ConversionResponse(
    @SerializedName("rate")
    val rate:Double) {


}