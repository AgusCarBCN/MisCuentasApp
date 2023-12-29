package carnerero.agustin.cuentaappandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.databinding.ItemPageBinding
import carnerero.agustin.cuentaappandroid.utils.Utils

class ViewTutorialAdapter(private var title:List<String>,private var description:List<String>,private var videoView:List<Int>):RecyclerView.Adapter<ViewTutorialAdapter.PagerViewHolder>() {


    inner class PagerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding = ItemPageBinding.bind(view)
        var titleItem=binding.tvTitleonboarding
        var desItem=binding.tvDesonboarding
        var img=binding.imgOnBoarding

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewTutorialAdapter.PagerViewHolder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.item_page,parent,false)
        return PagerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewTutorialAdapter.PagerViewHolder, position: Int) {
        holder.titleItem.text=title[position]
        holder.desItem.text=description[position]
        holder.img.setImageResource(videoView[position])
    }

    override fun getItemCount(): Int =title.size



}