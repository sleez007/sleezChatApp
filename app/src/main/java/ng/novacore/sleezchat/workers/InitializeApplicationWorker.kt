package ng.novacore.sleezchat.workers

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import timber.log.Timber

class InitializeApplicationWorker @WorkerInject constructor(@Assisted appContext: Context, @Assisted workerParams: WorkerParameters): CoroutineWorker(appContext,workerParams) {
    override suspend fun doWork(): Result {
        Timber.i("Starting work ${Thread.currentThread().name}")
        processJobs()
        return Result.failure()
    }

    private fun processJobs(){
        //Notify other users that user is online
        //retrieveContacts
        //checkIfActiveSession
        //getUserAppSettings
    }
}