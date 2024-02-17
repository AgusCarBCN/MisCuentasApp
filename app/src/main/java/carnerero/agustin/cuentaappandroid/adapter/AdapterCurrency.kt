package carnerero.agustin.cuentaappandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.model.CurrencyItem

class CustomAdapter(val context: Context, private val currencyList: Array<CurrencyItem>) : BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun getCount(): Int {
        return currencyList.size    }
    override fun getItem(position: Int): CurrencyItem {
        return currencyList[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val viewHolder: ViewHolder
        if (view == null) {
            view = inflater.inflate(R.layout.custom_choosecurrency, parent, false)
            viewHolder = ViewHolder(
                view.findViewById(R.id.imageFlag),
                view.findViewById(R.id.baseCurrency)
            )
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }
        val currencyItem = getItem(position)
        viewHolder.icon.setImageResource(currencyItem.currencyFlag)
        viewHolder.name.text = currencyItem.currencySymbol
        return view!!
    }
    private class ViewHolder(val icon: ImageView, val name: TextView)
}
