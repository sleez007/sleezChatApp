package ng.novacore.sleezchat.repository.localDataSource

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ng.novacore.sleezchat.db.AppDb
import ng.novacore.sleezchat.domain.ModelConverters
import ng.novacore.sleezchat.helper.SharedPrefHelper
import ng.novacore.sleezchat.model.data.MyContacts
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
}