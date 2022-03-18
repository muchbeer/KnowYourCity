package raum.muchbeer.knowyourcity.data

data class AgrienceApi(

    val primary_key : String,
    val user_name: String,
    val papdetails_out: List<BpapDetailOut>
) {
    data class BpapDetailOut(
       val grievance_out: List<CgrievanceOut>,
    )

    data class CgrievanceOut(
        val agreetosign: String,
        val full_name : String,
        val attachments_out: List<DpapAttachOut> = listOf()
    )

    data class DpapAttachOut(
        val file_name : String,
        val c_fullname: String,
        val url_name : String = ""
    )
}





