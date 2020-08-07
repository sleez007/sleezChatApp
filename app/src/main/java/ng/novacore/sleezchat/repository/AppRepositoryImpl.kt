package ng.novacore.sleezchat.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import dagger.hilt.android.qualifiers.ApplicationContext
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.db.entity.UsersEntity
import ng.novacore.sleezchat.domain.ModelConverters
import ng.novacore.sleezchat.internals.generics.GenericCb
import ng.novacore.sleezchat.model.SyncContacts
import ng.novacore.sleezchat.model.network.SyncContactsResponse
import ng.novacore.sleezchat.repository.localDataSource.LocalSourceInterface
import ng.novacore.sleezchat.repository.remoteDataSource.RemoteSourceInterface
import ng.novacore.sleezchat.utils.Result
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(val remoteSource: RemoteSourceInterface, val localSourceInterface: LocalSourceInterface, @ApplicationContext val context : Context): AppRepository {
    override suspend fun syncContacts(body: SyncContacts, cb: GenericCb<SyncContactsResponse>) {
       when(val response = remoteSource.syncContacts(body)) {
           is Result.Success ->{
               localSourceInterface.refreshContacts(response.data.contacts)
               cb.success(response.data)
           }
           is Result.Error->cb.error(response.exception.message ?: context.getString(R.string.network_error))
       }
    }

    override fun getMyContacts(): LiveData<PagedList<UsersEntity>> = localSourceInterface.getMyContacts()

}