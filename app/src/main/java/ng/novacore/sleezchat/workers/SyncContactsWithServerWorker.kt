package ng.novacore.sleezchat.workers

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ng.novacore.sleezchat.internals.generics.GenericCb
import ng.novacore.sleezchat.model.SyncContacts
import ng.novacore.sleezchat.model.network.SyncContactsResponse
import ng.novacore.sleezchat.repository.AppRepository
import ng.novacore.sleezchat.utils.ContactsUtil
import timber.log.Timber

class SyncContactsWithServerWorker @WorkerInject constructor(private val appRepository: AppRepository, @Assisted appContext: Context, @Assisted workerParams: WorkerParameters): CoroutineWorker(appContext,workerParams) {
   var result = Result.retry()

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO){
            syncContacts()
            result
        }
    }

    private suspend fun syncContacts(){
        val listener : GenericCb<SyncContactsResponse> = object :GenericCb<SyncContactsResponse>{
            override fun success(resp: SyncContactsResponse) {
                Timber.i("Network response is ----------------------")
                Timber.i(resp.toString())
                Timber.i("Network response close ----------------------")
                result = Result.success()
            }

            override fun error(msg: String) {
                Timber.i("Error Network response is ----------------------")
                Timber.i(msg)
                Timber.i("Error Network response close ----------------------")
                result
            }

        }
        val contactsList =   ContactsUtil.retrieveContacts(applicationContext)
        appRepository.syncContacts(SyncContacts( contactsList),listener)
        Timber.i(contactsList.toString())

    }

}