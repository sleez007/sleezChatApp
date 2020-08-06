package ng.novacore.sleezchat.repository

import ng.novacore.sleezchat.internals.generics.GenericCb
import ng.novacore.sleezchat.model.SyncContacts
import ng.novacore.sleezchat.model.network.SyncContactsResponse

interface AppRepository{
    suspend fun syncContacts(body: SyncContacts, cb: GenericCb<SyncContactsResponse>)
}