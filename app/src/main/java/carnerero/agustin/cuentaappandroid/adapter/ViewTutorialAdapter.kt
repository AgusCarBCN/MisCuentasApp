package carnerero.agustin.cuentaappandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import carnerero.agustin.cuentaappandroid.R
import carnerero.agustin.cuentaappandroid.databinding.ItemMovimientoBinding
import carnerero.agustin.cuentaappandroid.databinding.ItemPageBinding

class ViewTutorialAdapter(private var title:List<String>,private var tutorialText:List<String>,private var imageView:List<Int>):RecyclerView.Adapter<ViewTutorialAdapter.PagerViewHolder>() {


    inner class PagerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding = ItemPageBinding.bind(view)
        var titleItem=binding.tvTitleonboarding
        var img=binding.imgOnBoarding
        var descriptionText=binding.tvTutorial
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
        holder.descriptionText.text=tutorialText[position]
        holder.img.setImageResource(imageView[position])
    }

    override fun getItemCount(): Int =title.size



}