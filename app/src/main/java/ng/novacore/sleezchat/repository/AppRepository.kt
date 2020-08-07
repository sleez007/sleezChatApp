package ng.novacore.sleezchat.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import ng.novacore.sleezchat.db.entity.UsersEntity
import ng.novacore.sleezchat.internals.generics.GenericCb
import ng.novacore.sleezchat.model.SyncContacts
import ng.novacore.sleezchat.model.network.SyncContactsResponse

interface AppRepository{
    suspend fun syncContacts(body: SyncContacts, cb: GenericCb<SyncContactsResponse>)
    fun getMyContacts(): LiveData<PagedList<UsersEntity>>
}