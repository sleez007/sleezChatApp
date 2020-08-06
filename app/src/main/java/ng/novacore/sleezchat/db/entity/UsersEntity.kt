package ng.novacore.sleezchat.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
// indices = [Index(value = ["phoneQuery"],unique = true)]
@Entity(tableName = "user_model")
data class UsersEntity(
    @PrimaryKey(autoGenerate = false)
    val _id : String,
    var contactID :Int = 0,
    var contactName: String? = null,
    var phoneNumber : String? = null,
    var phoneQuery: String? =null,
    var linked: Boolean = false,
    var active: Boolean = false,
    var exists : Boolean = false,
    var image: String? = null,
    var statusMsg: String? = null
)