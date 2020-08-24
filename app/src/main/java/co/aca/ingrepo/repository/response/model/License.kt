package co.aca.ingrepo.repository.response.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class License : Serializable {


    @SerializedName("key")
    val key: String? = null
    @SerializedName("name")
    val name: String? = null
    @SerializedName("spdx_id")
    val spdx_id: String? = null
    @SerializedName("url")
    val url: String? = null
    @SerializedName("node_id")
    val nodeId: String? = null



}
