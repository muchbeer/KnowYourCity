package raum.muchbeer.knowyourcity.presentation.totalfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import raum.muchbeer.knowyourcity.data.AgrienceModel
import raum.muchbeer.knowyourcity.data.BpapDetailModel
import raum.muchbeer.knowyourcity.data.CgrievanceModel
import raum.muchbeer.knowyourcity.data.DpapAttachEntity
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

        val username = binding.usernameEdt.text
        val agreetosign = binding.agreeToSign.text
        val fullNameVal = binding.fullName.text
        val filenameEdt = binding.filename.text
        val fileUrlEdt = binding.fileUrl.text

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

        viewModel.getAllDAttachWithfullName("Gianna").observe(viewLifecycleOwner) {
            dAttachmentList.clear()
            dAttachmentList.addAll(it)
       }

        viewModel.getAllBGrievWithSameUsername(username = "muchbeer").observe(viewLifecycleOwner) {
            bpapsTopList.clear()
            bpapsTopList.addAll(it)
        }


        val dAttach = DpapAttachEntity(
            file_name = "giannaFilename",
            url_name = "giannaUrl",
            c_fullname = "Gianna")

        binding.btnDAttach.setOnClickListener {
            viewModel.insertDattach(dattach = dAttach)
            Log.d(TAG, "tHE cGriev value is : ${dAttach}")
        }

        val cGriev = CgrievanceModel(
            agreetosign = "yes",
            full_name = "Gianna",
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

    private fun displayValue() {






       binding.apply {


       }
    }

companion object {
    private val TAG = HomeFragment::class.simpleName
}
}