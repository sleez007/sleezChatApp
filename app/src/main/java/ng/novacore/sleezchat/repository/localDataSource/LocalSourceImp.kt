package ng.novacore.sleezchat.repository.localDataSource

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ng.novacore.sleezchat.db.AppDb
import ng.novacore.sleezchat.db.entity.UsersEntity
import ng.novacore.sleezchat.domain.ModelConverters
import ng.novacore.sleezchat.helper.SharedPrefHelper
import ng.novacore.sleezchat.model.data.MyContacts
import ng.novacore.sleezchat.utils.Constants
import javax.inject.Inject

class LocalSourceImp @Inject constructor(val db: AppDb, val sharedPrefHelper: SharedPrefHelper, private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO): LocalSourceInterface {

    /**
     * @param contacts holds a network domain model of contact
     * @return void
     * @desc Write new contacts to room database
     */
    override suspend fun refreshContacts(contacts: List<MyContacts>) {
        withContext(ioDispatcher){
            val users  = ModelConverters.convertNetworkContactsToUserDaoModel(contacts);
            db.getUserDao().refreshContacts(users)
        }
    }

    override  fun getMyContacts(): LiveData<PagedList<UsersEntity>> {
        val dataSource = db.getUserDao().retrieveUserList()
        return LivePagedListBuilder(dataSource, Constants.DATABASE_PAGE_SIZE).build()
    }
}