package carnerero.agustin.cuentaappandroid.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.model.MovimientoBancario

class AdapterMov(private val movList:ArrayList<MovimientoBancario>):RecyclerView.Adapter<AdapterMov.ViewHolder>() {

    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.item_mov,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
    return movList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val mov=movList[position]
        holder.descripcionitem.text=mov.descripcion
        holder.importeitem.text=mov.importe.toString()
        holder.fechaitem.text=mov.fechaImporte
    }
    //Aqui se obtienen los movimientos que se han realizado en la cuenta
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        var idMov:Int=0
        var importeitem: TextView=itemView.findViewById(R.id.tv_importeitem)
        var descripcionitem: TextView=itemView.findViewById(R.id.tv_descripcionitem)
        var fechaitem: TextView=itemView.findViewById(R.id.tv_fechaimporteitem)
        var mov:Int=0

    }
}