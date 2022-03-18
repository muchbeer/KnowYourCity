package raum.muchbeer.knowyourcity.data

import raum.muchbeer.knowyourcity.data.AgrienceApi.*

class ModelMapper : ModelMapperInterface<AgrienceApi, AgrienceModel> {

    override fun mapFromEntity(domainEntity: AgrienceModel): AgrienceApi {
    return AgrienceApi(
        primary_key = domainEntity.primary_key,
        user_name  = domainEntity.user_name,
        papdetails_out = fromBapListEntity(domainEntity.papdetails)
       )
    }

    private fun mapFromBPapDetailEntity(bpapEntity: BpapDetailModel): BpapDetailOut {
       return BpapDetailOut(
           grievance_out = fromCgrieListEntity(bpapEntity.grievance)
       )
    }

   private fun mapFromCgrievanceEntity(cgrievanceEntity : CgrievanceModel) : CgrievanceOut {
        return CgrievanceOut(
            agreetosign = cgrievanceEntity.agreetosign,
            full_name = cgrievanceEntity.full_name,
            attachments_out = fromDpapListEntity(cgrievanceEntity.attachments)
        )
    }

   private fun mapFromDgrievanceEntity(dattachEntity : DpapAttachEntity) : DpapAttachOut {
        return DpapAttachOut(
            file_name = dattachEntity.file_name,
            c_fullname = dattachEntity.c_fullname,
            url_name = dattachEntity.url_name
        )
    }

  private  fun fromBapListEntity(bPapEntityList: List<BpapDetailModel>): List<BpapDetailOut>{
        return bPapEntityList.map { mapFromBPapDetailEntity(it) }
    }

   private fun fromCgrieListEntity(cGrievEntityList : List<CgrievanceModel>) : List<CgrievanceOut> {
        return cGrievEntityList.map { mapFromCgrievanceEntity(it) }
    }

   private fun fromDpapListEntity(dattachEntityList : List<DpapAttachEntity>) : List<DpapAttachOut> {
        return dattachEntityList.map { mapFromDgrievanceEntity(it) }
    }
}

interface ModelMapperInterface <T, Entity>{

    fun mapFromEntity(domainEntity: Entity): T

}