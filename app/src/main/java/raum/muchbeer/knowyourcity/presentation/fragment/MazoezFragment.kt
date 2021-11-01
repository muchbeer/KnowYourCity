package raum.muchbeer.knowyourcity.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import raum.muchbeer.knowyourcity.R
import raum.muchbeer.knowyourcity.presentation.viewmodel.zoez.MazoeziVM

class MazoezFragment : Fragment() {

    private val mazoeziVM : MazoeziVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mazoez, container, false)
    }

}