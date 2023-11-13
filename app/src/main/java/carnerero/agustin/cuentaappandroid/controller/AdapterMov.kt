package carnerero.agustin.cuentaappandroid.controller

import android.content.Context
import android.icu.text.NumberFormat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario
import carnerero.agustin.cuentaappandroid.utils.Utils
import java.text.SimpleDateFormat

import java.util.Locale

class AdapterMov(private val movList:ArrayList<MovimientoBancario>):RecyclerView.Adapter<AdapterMov.ViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.item_movimiento,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
    return movList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lang= Utils.getLang()
        val country=Utils.getCountry()

        val euroLocale = Locale(lang, country)
        val currencyFormat = NumberFormat.getCurrencyInstance(euroLocale)
        //val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val mov = movList[position]
        holder.descripcionitem.text = mov.descripcion
        // Mostrar la fecha si es diferente de la fecha anterior
        if (position == 0 || mov.fechaImporte != movList[position - 1].fechaImporte) {
            holder.fechaitem.text = formatFriendlyDate(mov.fechaImporte)
            holder.fechaitem.visibility = View.VISIBLE
        } else {
            holder.fechaitem.visibility = View.GONE       }


        val importeSinSigno = if (mov.importe < 0) -mov.importe else mov.importe
        holder.importeitem.text =currencyFormat.format(importeSinSigno)
        holder.importeitem.setTextColor(
            ContextCompat.getColor(
                holder.importeitem.context,
                if (mov.importe < 0) R.color.red else R.color.darkGreenPistacho
            )
        )

    }
    //Aqui se obtienen los movimientos que se han realizado en la cuenta
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        //var idMov:Int=0
        var importeitem: TextView=itemView.findViewById(R.id.tv_impitem)
        var descripcionitem: TextView=itemView.findViewById(R.id.tv_desitem)
        var fechaitem: TextView=itemView.findViewById(R.id.tv_dateitem)
        //var mov:Int=0

    }
    fun formatFriendlyDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("es", "ES"))

        try {
            val date = inputFormat.parse(inputDate)
            if (date != null) {
                return outputFormat.format(date)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return inputDate // Devuelve la fecha original si hay un error en el formato
    }

}