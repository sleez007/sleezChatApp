package ng.novacore.sleezchat.domain

import ng.novacore.sleezchat.db.entity.UsersEntity
import ng.novacore.sleezchat.model.data.MyContacts

object ModelConverters {
    /**
     * @param contacts a list of network returned contacts
     */
    fun convertNetworkContactsToUserDaoModel(contacts: List<MyContacts>): List<UsersEntity>{
        val dbLayer = contacts.map {
            UsersEntity(
                _id = it.id,
                contactID = it.contactId,
                contactName = it.username,
                phoneNumber = it.phone,
                phoneQuery = it.phoneQuery,
                linked = it.linked,
                active = it.activated,
                exists = it.exist,
                image = it.image,
                statusMsg = it.status?.body
            )
        }
        return dbLayer
    }
}