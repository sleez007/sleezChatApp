package ng.novacore.sleezchat.model

import ng.novacore.sleezchat.db.entity.UsersEntity

data class SyncContacts (
    val userList: List<UsersEntity>
)