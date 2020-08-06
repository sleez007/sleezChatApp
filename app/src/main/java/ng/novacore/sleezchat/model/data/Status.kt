package ng.novacore.sleezchat.model.data


import com.google.gson.annotations.SerializedName

data class Status(
    val body: String,
    val created: String,
    val current: Boolean,
    @SerializedName("_id")
    val id: String,
    val userId: String
)