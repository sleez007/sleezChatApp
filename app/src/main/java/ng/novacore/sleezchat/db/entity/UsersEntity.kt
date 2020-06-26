package ng.novacore.sleezchat.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user_model", indices = [Index(value = ["phoneQuery"],unique = true)])
data class UsersEntity(
    @PrimaryKey(autoGenerate = true)
    val _id : Long =0L,
    var contactID :Long = 0L,
    var contactName: String? = null,
    var phoneNumber : String? = null,
    var phoneQuery: String? =null,
    var linked: Boolean = false,
    var active: Boolean = false,
    var exists : Boolean = false,
    var image: String? = null
)