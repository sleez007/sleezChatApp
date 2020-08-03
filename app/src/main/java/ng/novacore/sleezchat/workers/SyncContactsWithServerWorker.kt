package ng.novacore.sleezchat.workers

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ng.novacore.sleezchat.utils.ContactsUtil
import timber.log.Timber

class SyncContactsWithServerWorker @WorkerInject constructor(@Assisted appContext: Context, @Assisted workerParams: WorkerParameters): CoroutineWorker(appContext,workerParams) {
    override suspend fun doWork(): Result {
        syncContacts()
        return Result.success()
    }

    private fun syncContacts(){
      val contactsList =   ContactsUtil.retrieveContacts(applicationContext)
        Timber.i(contactsList.toString())
    }

}