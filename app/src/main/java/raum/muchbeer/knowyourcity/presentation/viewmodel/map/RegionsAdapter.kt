package raum.muchbeer.knowyourcity.presentation.viewmodel.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import raum.muchbeer.knowyourcity.data.Region
import raum.muchbeer.knowyourcity.databinding.RegionItemBinding

class RegionsAdapter(private val onClickListener: RegionClickListener) : RecyclerView.Adapter<RegionsVH>() {

    private var regions : List<Pair<Region, Boolean>> = arrayListOf<Pair<Region, Boolean>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionsVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RegionItemBinding.inflate(layoutInflater, parent, false)

      return  RegionsVH(binding)
    }

    override fun onBindViewHolder(holder: RegionsVH, position: Int) {
        holder.dataBind(regions[position], onClickListener)
    }

    fun setRegions(allRegions: List<Pair<Region, Boolean>>) {
        regions = allRegions
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return regions.size
    }

   inner class  regionCallBack : DiffUtil.ItemCallback<Region>() {
        override fun areItemsTheSame(oldItem: Region, newItem: Region): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Region, newItem: Region): Boolean {
            return oldItem.id == newItem.id
        }
    }
}

class RegionsVH(val binding: RegionItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun dataBind(pair: Pair<Region, Boolean>, clickListener: RegionClickListener) {
        binding.apply {
            item.setOnClickListener { clickListener.onClick(pair.first.id) }
            title.text = pair.first.title
            colorBlock.setBackgroundColor(pair.first.color.toColorInt())
            title.isChecked = pair.second
        }
    }

}

interface RegionClickListener {
    fun onClick(id: Int)
}
