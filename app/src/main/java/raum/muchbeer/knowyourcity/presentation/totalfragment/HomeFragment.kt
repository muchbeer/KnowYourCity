package raum.muchbeer.knowyourcity.presentation.totalfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import raum.muchbeer.knowyourcity.data.*
import raum.muchbeer.knowyourcity.databinding.FragmentHomeBinding
import raum.muchbeer.knowyourcity.presentation.viewmodel.total.TotalViewModel

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private val viewModel : TotalViewModel by viewModels()

    private val bpapsTopList = mutableListOf<BpapDetailModel>()
    private val cgrievList = mutableListOf<CgrievanceModel>()
    private val dAttachmentList =  mutableListOf<DpapAttachEntity>()
        private lateinit var primarKey : String
    private lateinit var valuationNo : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        primarKey = "tech02"
        valuationNo = "tech03"

        val gsonPretty = GsonBuilder().setPrettyPrinting().create()


        binding.pK.setOnClickListener {
            primarKey = (10..100).random().toString()
            Toast.makeText(requireContext(),
                "The Key is : ${primarKey}", Toast.LENGTH_LONG).show()
        }

        binding.valutationNumber.setOnClickListener {
            valuationNo = (100..200).random().toString()
            Toast.makeText(requireContext(),
                "The valuation number is : ${valuationNo}", Toast.LENGTH_LONG).show()
        }


        viewModel.getAllCGrievWithSameUsername("muchbeer").observe(viewLifecycleOwner) {
            cgrievList.clear()
            cgrievList.addAll(it)   }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getAllDAttachByStatus(imagestatus = IMAGESTATUS.SUCCESSFUL).collect {
                dAttachmentList.clear()
                dAttachmentList.addAll(it)
            }
        }


        viewModel.getAllBGrievWithSameUsername(username = "muchbeer").observe(viewLifecycleOwner) {
            bpapsTopList.clear()
            bpapsTopList.addAll(it)
        }


        val dAttach = DpapAttachEntity(
            file_name = "georgeFilename",
            url_name = "georgeUrl",
            c_fullname = "George")

        binding.btnDAttach.setOnClickListener {
            viewModel.insertDattach(dattach = dAttach)
            Log.d(TAG, "tHE cGriev value is : ${dAttach}")
        }

        val cGriev = CgrievanceModel(
            agreetosign = "yes",
            full_name = "George",
            a_username = "muchbeer",
            attachments = dAttachmentList)
        binding.btnCGrievance.setOnClickListener {
            Log.d(TAG, "tHE value attached is : ${gsonPretty.toJson(dAttachmentList)}")
            viewModel.insertCgriev(cGriev)

            Log.d(TAG, "tHE cGriev value is : ${cGriev}")
        }


        val bAttachTop = BpapDetailModel(
            "B01",
            a_username = "muchbeer",
            grievance = cgrievList
             )

        binding.btnBpapDetail.setOnClickListener {
            viewModel.updatCgrievance(cGriev.copy(attachments = dAttachmentList))
            viewModel.insertBpapsEntry(bAttachTop)

            Log.d(TAG, "tHE cGriev value is : ${bAttachTop}")
        }

        val aGrievanceGeneral = AgrienceModel(
            primary_key= "A01",
            user_name = "muchbeer",
            papdetails = listOf(bAttachTop) )
        binding.btnAgrievance.setOnClickListener {
            viewModel.insertAgrievEntry( agriev = aGrievanceGeneral )
            Log.d(TAG, "tHE cGriev value is : ${aGrievanceGeneral}")
            //clear all text
            //    filename.text.clear()
            //    fileUrl.text.clear()
            //    fullName.text.clear()
            //  agreeToSign.text.clear()
            //   valutationNumber.text.clear()
            //  usernameEdt.text.clear()
            //  pK.text.clear()
        }


        binding.btnDisplayAOnly.setOnClickListener {
            viewModel.allAgrienceEntry.observe(viewLifecycleOwner) {
                val  agrievance: String = gsonPretty.toJson(it)
                Log.d(TAG, "The AGrievence : ${agrievance}")
                viewModel.displayApiModel(it)
            }
        }

        binding.btnViewBOnly.setOnClickListener {
            viewModel.allBpapsEntry.observe(viewLifecycleOwner) {
                val bcd = gsonPretty.toJson(it)
                Log.d(TAG, "tHE BPapsWithCD is : ${bcd} ")
            }
        }

        binding.btnViewCGrievance.setOnClickListener {
            viewModel.allCpapsDetailEntry.observe(viewLifecycleOwner) {
                val ab = gsonPretty.toJson(it)
                Log.d(TAG, "The CGrievance is : ${ab}")
            }
        }

        binding.btnViewDAttach.setOnClickListener {
            viewModel.allDAttachmentEntry.observe(viewLifecycleOwner) {
                val dpaps = gsonPretty.toJson(it)
                Log.d(TAG, "tHE value of Dpaps is : ${dpaps}")
            }
        }
         //   displayValue()
        return binding.root
    }


companion object {
    private val TAG = HomeFragment::class.simpleName
}
}