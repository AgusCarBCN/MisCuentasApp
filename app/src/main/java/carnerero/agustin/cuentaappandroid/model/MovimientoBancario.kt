package carnerero.agustin.cuentaappandroid.model


import java.text.SimpleDateFormat
import java.util.Date
import android.os.Parcel
import android.os.Parcelable
import java.util.Locale


data class MovimientoBancario(
    val importe: Double,
    val descripcion: String,
    val iban: String,
    val fechaImporte: String = SimpleDateFormat("dd/MM/yyyy").format(Date())

) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: SimpleDateFormat("dd/MM/yyyy").format(Date())
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(importe)
        parcel.writeString(descripcion)
        parcel.writeString(iban)
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

