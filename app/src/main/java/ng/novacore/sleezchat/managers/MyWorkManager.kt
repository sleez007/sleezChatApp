package ng.novacore.sleezchat.managers


import android.content.Context
import androidx.work.*
import ng.novacore.sleezchat.workers.InitializeApplicationWorker
import ng.novacore.sleezchat.workers.SyncContactsWithServerWorker
import java.util.concurrent.TimeUnit


object MyWorkManager {
    fun initializeAppWorkers(context: Context){
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val workRequest : OneTimeWorkRequest = OneTimeWorkRequestBuilder<InitializeApplicationWorker>().setConstraints(constraints).addTag(InitializeApplicationWorker::class.java.name).build()
        WorkManager.getInstance(context).beginUniqueWork(InitializeApplicationWorker::class.java.name, ExistingWorkPolicy.REPLACE,workRequest).enqueue()
    }

    fun synchronizeContactsWithServer(context : Context){
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val periodicWorkRequest :PeriodicWorkRequest = PeriodicWorkRequestBuilder<SyncContactsWithServerWorker>(12,TimeUnit.HOURS,5,TimeUnit.MINUTES).setConstraints(constraints).addTag(SyncContactsWithServerWorker::class.java.name).build()
        WorkManager.getInstance(context).enqueue(periodicWorkRequest)
    }
}