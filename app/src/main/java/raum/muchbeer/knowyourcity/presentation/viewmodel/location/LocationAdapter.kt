package raum.muchbeer.knowyourcity.presentation.viewmodel.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import raum.muchbeer.knowyourcity.data.Activity
import raum.muchbeer.knowyourcity.data.Location
import raum.muchbeer.knowyourcity.databinding.LocationActivityItemBinding

class LocationAdapter() : ListAdapter<Activity, LocationAdapter.LocVH>(difLoc) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocVH {
       val layoutInflator = LayoutInflater.from(parent.context)
        val binding = LocationActivityItemBinding.inflate(layoutInflator, parent, false)

        return LocVH(binding)
    }

    override fun onBindViewHolder(holder: LocVH, position: Int) {
        holder.bindData(getItem(position))
    }

    class LocVH(val binding : LocationActivityItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bindData(data: Activity) {
                binding.apply {
                    title.text = data.title

                    val iconUri = "drawable/ic_${data.icon}_black_24dp"
                    val imageResource: Int =
                        itemView.resources.getIdentifier(
                            iconUri, null, itemView.context.packageName
                        )
                    icon.setImageResource(imageResource)
                    icon.contentDescription = data.title
                }
            }
    }

    companion object difLoc : DiffUtil.ItemCallback<Activity>() {
        override fun areItemsTheSame(oldItem: Activity, newItem: Activity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Activity, newItem: Activity): Boolean {
            return oldItem.activityId == newItem.activityId
        }

    }
}

