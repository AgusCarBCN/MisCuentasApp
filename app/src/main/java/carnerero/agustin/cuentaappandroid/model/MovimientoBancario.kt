package carnerero.agustin.cuentaappandroid.model



import java.util.Date
import android.os.Parcel
import android.os.Parcelable
import carnerero.agustin.cuentaappandroid.utils.dateFormat


data class MovimientoBancario(
    val importe: Double,
    val descripcion: String,
    val nombreDeCuenta: String,
    val fechaImporte: String = Date().dateFormat()

) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: Date().dateFormat()
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(importe)
        parcel.writeString(descripcion)
        parcel.writeString(nombreDeCuenta)
        parcel.writeString(fechaImporte)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MovimientoBancario> {
        override fun createFromParcel(parcel: Parcel): MovimientoBancario {
            return MovimientoBancario(parcel)
        }

        override fun newArray(size: Int): Array<MovimientoBancario?> {
            return arrayOfNulls(size)
        }
    }
}

