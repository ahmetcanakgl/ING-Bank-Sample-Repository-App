package co.aca.ingrepo.repository.base

import com.google.gson.annotations.SerializedName

open class BaseResponse {

    @SerializedName("page")
    var page : Int? = null

    @SerializedName("total_results")
    var total_results : Int? = null

    @SerializedName("total_pages")
    var total_pages : Int? = null

    @SerializedName("errors")
    var errors : ArrayList<String>? = null

}