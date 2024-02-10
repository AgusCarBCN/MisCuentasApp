package carnerero.agustin.cuentaappandroid.adapter

import android.icu.text.NumberFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import carnerero.agustin.cuentaappandroid.interfaces.OnLocaleListener
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.databinding.ItemSaldoBinding
import carnerero.agustin.cuentaappandroid.model.Cuenta
import carnerero.agustin.cuentaappandroid.utils.Utils

class AdapterBal(
    private var listaDeCuentas: MutableList<Cuenta>,
    private val listener: OnLocaleListener
) :
    RecyclerView.Adapter<AdapterBal.ViewHolder>() {
    //Esta clase permite inflar la vista item_saldo
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemSaldoBinding.bind(view)
        var nombreDeCuenta = binding.tvCuenta
        var saldoDeCuenta = binding.tvSaldo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_saldo, parent, false)
        return ViewHolder(itemView)
    }
    override fun getItemCount(): Int = listaDeCuentas.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val locale = listener.getLocale()
        val conversionRate=listener.getConversionRate()
        val currencyFormat = NumberFormat.getCurrencyInstance(locale)
        val cuenta = listaDeCuentas[position]
        holder.nombreDeCuenta.text = cuenta.nombre
        val balance = cuenta.saldo*conversionRate
        holder.saldoDeCuenta.text = currencyFormat.format(balance)
        val textColorResId = if (Utils.isDarkTheme) R.color.darkGreenPistacho else R.color.darkgreen
        val textColor = ContextCompat.getColor(holder.saldoDeCuenta.context, textColorResId)
        holder.saldoDeCuenta.setTextColor(textColor)

    }


}