package raum.muchbeer.knowyourcity.presentation.viewmodel.location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import raum.muchbeer.knowyourcity.R
import raum.muchbeer.knowyourcity.data.Location
import raum.muchbeer.knowyourcity.databinding.LocationItemBinding

class LocationsAdapter(
    private val clickListener: OnLocationClickListener) :
                ListAdapter<Location, LocationsAdapter.LocVH>(diffLoc) {

    private var currentLocation: android.location.Location? = null

    fun setCurrentLocation(location: android.location.Location) {
        currentLocation = location
      //  allLocations = allLocations.sortedBy { it.getDistanceInMiles(location) }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LocationItemBinding.inflate(inflater, parent, false)

        return LocVH(binding)
    }

    override fun onBindViewHolder(holder: LocVH, position: Int) {
       val data = getItem(position)
        holder.bind(data, clickListener)
    }

   inner class LocVH(val binding: LocationItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data : Location, clickListener: OnLocationClickListener) {
            binding.title.text = data.title
            binding.card.setOnClickListener { clickListener.onClick(data.locationId) }

            if (currentLocation != null) {
                binding.distanceIcon.visibility = View.VISIBLE

                binding.distance.visibility = View.VISIBLE
                binding.distance.text = itemView.context.getString(
                    R.string.distance_value,
                    data.getDistanceInMiles(currentLocation!!)
                )
            }
        }
    }

    companion object diffLoc : DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
           return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem.locationId == newItem.locationId
        }

    }
}

interface OnLocationClickListener {
    fun onClick(id: Int)
}