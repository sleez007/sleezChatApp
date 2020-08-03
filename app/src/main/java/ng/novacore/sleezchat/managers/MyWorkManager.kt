package ng.novacore.sleezchat.managers


import android.content.Context
import androidx.work.*
import ng.novacore.sleezchat.workers.InitializeApplicationWorker
import ng.novacore.sleezchat.workers.SyncContactsWithServerWorker
import java.util.concurrent.TimeUnit


object MyWorkManager {
    fun initializeAppWorkers(context: Context) {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val workRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<InitializeApplicationWorker>().setConstraints(constraints)
                .addTag(InitializeApplicationWorker::class.java.name).setBackoffCriteria(
                BackoffPolicy.LINEAR,
                OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            ).build()
        WorkManager.getInstance(context).beginUniqueWork(
            InitializeApplicationWorker::class.java.name,
            ExistingWorkPolicy.REPLACE,
            workRequest
        ).enqueue()
    }

    fun synchronizeContactsWithServerAsap(context: Context){
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val workRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<SyncContactsWithServerWorker>().setConstraints(constraints)
                .addTag(SyncContactsWithServerWorker::class.java.name).build()
        WorkManager.getInstance(context).beginUniqueWork(
            SyncContactsWithServerWorker::class.java.name,
            ExistingWorkPolicy.REPLACE,
            workRequest
        ).enqueue()
    }

    fun synchronizeContactsWithServerPeriodic(context: Context) {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val periodicWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<SyncContactsWithServerWorker>(
                12,
                TimeUnit.HOURS,
                15,
                TimeUnit.MINUTES
            ).setConstraints(constraints).addTag(SyncContactsWithServerWorker::class.java.name)
                .build()
        WorkManager.getInstance(context).enqueue(periodicWorkRequest)
    }
}