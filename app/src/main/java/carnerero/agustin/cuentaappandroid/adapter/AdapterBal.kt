package carnerero.agustin.cuentaappandroid.adapter

import android.icu.text.NumberFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import carnerero.agustin.cuentaappandroid.OnLocaleListener
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.databinding.ItemSaldoBinding
import carnerero.agustin.cuentaappandroid.model.Cuenta
import carnerero.agustin.cuentaappandroid.utils.Utils

class AdapterBal(
    private var accountsList: MutableList<Cuenta>,
    private val listener: OnLocaleListener
) :
    RecyclerView.Adapter<AdapterBal.ViewHolder>() {
    //Esta clase permite inflar la vista item_saldo
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemSaldoBinding.bind(view)
        var ibanAccount = binding.tvCuenta
        var balanceAccount = binding.tvSaldo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_saldo, parent, false)
        return ViewHolder(itemView)
    }
    override fun getItemCount(): Int = accountsList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val locale = listener.getLocale()
        val currencyFormat = NumberFormat.getCurrencyInstance(locale)
        val cuenta = accountsList[position]
        holder.ibanAccount.text = cuenta.iban
        val balance = cuenta.saldo
        holder.balanceAccount.text = currencyFormat.format(balance)
        val textColorResId = if (Utils.isDarkTheme) R.color.darkGreenPistacho else R.color.darkgreen
        val textColor = ContextCompat.getColor(holder.balanceAccount.context, textColorResId)
        holder.balanceAccount.setTextColor(textColor)

    }


}