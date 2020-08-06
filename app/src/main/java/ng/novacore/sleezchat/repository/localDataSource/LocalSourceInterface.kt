package ng.novacore.sleezchat.repository.localDataSource

import ng.novacore.sleezchat.model.data.MyContacts

interface LocalSourceInterface{
    suspend fun refreshContacts(contacts : List<MyContacts>)
}