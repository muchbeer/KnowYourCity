package raum.muchbeer.knowyourcity.presentation.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import raum.muchbeer.knowyourcity.R
import raum.muchbeer.knowyourcity.databinding.DialogFilterBinding
import raum.muchbeer.knowyourcity.presentation.viewmodel.map.MapViewModel
import raum.muchbeer.knowyourcity.presentation.viewmodel.map.RegionClickListener
import raum.muchbeer.knowyourcity.presentation.viewmodel.map.RegionsAdapter

class FilterDialogFragment : DialogFragment(), RegionClickListener {

    private val mapViewModel: MapViewModel by viewModels()
    private lateinit var adapter: RegionsAdapter
    private lateinit var bindingView : DialogFilterBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
     val dialogView =   activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            bindingView = DialogFilterBinding.inflate(inflater, null, false)
         //   customView = inflater.inflate(R.layout.dialog_filter, null)
            builder.setView(bindingView.root)
                .setTitle("Regions")
                .setPositiveButton(R.string.ok, { _, _ -> })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")



        return dialogView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

      //  binding = DialogFilterBinding.bind(view)
        adapter = RegionsAdapter(this)
        bindingView.listRegions.adapter = adapter

      /*  mapViewModel.selectedRegions.observe(viewLifecycleOwner)  {
            adapter.setRegions(it)
        }*/

        bindingView.customButton.setOnClickListener {
            mapViewModel.setBeginCustomDraw(true)
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = bindingView.root

    override fun onClick(id: Int) {
       // mapViewModel.toggleVisibleRegion(id)
    }
}