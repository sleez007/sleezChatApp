package ng.novacore.sleezchat.repository.localDataSource

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import ng.novacore.sleezchat.db.entity.UsersEntity
import ng.novacore.sleezchat.model.data.MyContacts

interface LocalSourceInterface{
    suspend fun refreshContacts(contacts : List<MyContacts>)

    fun getMyContacts(): LiveData<PagedList<UsersEntity>>
}