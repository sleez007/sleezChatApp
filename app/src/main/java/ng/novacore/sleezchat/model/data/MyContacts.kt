package ng.novacore.sleezchat.model.data
import com.google.gson.annotations.SerializedName

data class MyContacts(
    val activated: Boolean,
    val contactId: Int,
    val exist: Boolean,
    @SerializedName("_id")
    val id: String,
    val image: String,
    val linked: Boolean,
    val phone: String,
    val phoneQuery : String,
    val status: Status?,
    val username: String
)