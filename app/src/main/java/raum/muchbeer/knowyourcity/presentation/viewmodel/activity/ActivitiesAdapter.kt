package raum.muchbeer.knowyourcity.presentation.viewmodel.activity

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import raum.muchbeer.knowyourcity.R
import raum.muchbeer.knowyourcity.data.Activity
import raum.muchbeer.knowyourcity.databinding.ActivityItemBinding

class ActivitiesAdapter(
    private val context : Context,
    private val onClickListener: OnClickListener) :
                    ListAdapter<Activity, ActivitiesAdapter.ActivityVH>(diffUtil) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActivityVH {

      val inflater = LayoutInflater.from(parent.context)
      val binding = ActivityItemBinding.inflate(inflater, parent, false)

        return ActivityVH(binding)
    }

    override fun onBindViewHolder(holder: ActivitiesAdapter.ActivityVH, position: Int) {
        val data = getItem(position)
        holder.bind(data, clickListener = onClickListener)
    }

   inner class ActivityVH(val binding : ActivityItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind (data : Activity, clickListener: OnClickListener) {
            Log.d("AdapterAct", "In the adapter data is: ${data.title}")
            binding.title.text = data.title

            binding.card.setOnClickListener {
                clickListener.onClick(data.activityId, data.title)
            }
            binding.geofence.setOnClickListener { clickListener.onGeofenceClick(data.activityId) }

            var color = R.color.colorGray
            if (data.geofenceEnabled) {
                color = R.color.teal_700
            }

           /* GlideApp.with(holder.itemView.context)
                .load(badgeUrl)
                .into(binding.topLearnerImage)*/


            ImageViewCompat
                .setImageTintList(
                    binding.geofence,
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            binding.geofence.context,
                            color
                        )
                    )
                )

            val iconUri = "drawable/ic_${data.icon}_black_24dp"
            val imageResource: Int =
                context.resources.getIdentifier(
                    iconUri, null, context.packageName
                )
            binding.icon.setImageResource(imageResource)
            binding.icon.contentDescription = data.title
        }
    }

    companion object diffUtil : DiffUtil.ItemCallback<Activity>() {
        override fun areItemsTheSame(oldItem: Activity, newItem: Activity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Activity, newItem: Activity): Boolean {
           return oldItem.activityId == newItem.activityId
        }

    }
}

interface OnClickListener {
    fun onClick(id: Int, title: String)
    fun onGeofenceClick(id: Int)
}