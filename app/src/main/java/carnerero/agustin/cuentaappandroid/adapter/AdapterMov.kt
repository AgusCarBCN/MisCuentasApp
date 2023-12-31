package carnerero.agustin.cuentaappandroid.adapter


import android.icu.text.NumberFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import carnerero.agustin.cuentaappandroid.interfaces.OnLocaleListener
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.databinding.ItemMovimientoBinding
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario
import carnerero.agustin.cuentaappandroid.utils.Utils
import java.text.SimpleDateFormat

import java.util.Locale

class AdapterMov(private var movList: MutableList<MovimientoBancario>,private val listener : OnLocaleListener) :
    RecyclerView.Adapter<AdapterMov.ViewHolder>() {

    //Esta clase permite inflar la vista item_movimiento
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding = ItemMovimientoBinding.bind(view)
        var importeitem: TextView = binding.tvImpitem
        var descripcionitem: TextView = binding.tvDesitem
        var fechaitem: TextView = binding.tvDateitem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.item_movimiento,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = movList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val conversionRate=listener.getConversionRate()
        val locale = listener.getLocale()
        val currencyFormat = NumberFormat.getCurrencyInstance(locale)
        val mov = movList[position]
        holder.descripcionitem.text = mov.descripcion
        holder.descripcionitem.setTextColor(
            ContextCompat.getColor(
                holder.descripcionitem.context,
                if(Utils.isDarkTheme)R.color.lightblue else R.color.blue
            )

        )
        // Mostrar la fecha si es diferente de la fecha anterior
        if (position == 0 || mov.fechaImporte != movList[position - 1].fechaImporte) {
            holder.fechaitem.text = formatFriendlyDate(mov.fechaImporte)
            holder.fechaitem.visibility = View.VISIBLE
        } else {
            holder.fechaitem.visibility = View.GONE
        }
        val importeSinSigno = if (mov.importe < 0) -mov.importe else mov.importe
        holder.importeitem.text = currencyFormat.format(importeSinSigno*conversionRate)
        val textColorResId = when {
            mov.importe < 0 && Utils.isDarkTheme -> R.color.coralred
            mov.importe < 0 -> R.color.red
            mov.importe > 0 && Utils.isDarkTheme -> R.color.darkGreenPistacho
            else -> R.color.darkgreen
        }

        val textColor = ContextCompat.getColor(holder.importeitem.context, textColorResId)
        holder.importeitem.setTextColor(textColor)

    }

    private fun formatFriendlyDate(inputDate: String): String {
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