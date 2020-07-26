package ng.novacore.sleezchat.application

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.DefineComponent
import dagger.hilt.android.HiltAndroidApp
import ng.novacore.sleezchat.helper.AppSignatureHelper
import ng.novacore.sleezchat.managers.MyWorkManager
import timber.log.Timber
import javax.inject.Inject


@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration = Configuration.Builder().setWorkerFactory(workerFactory).build()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        initializeTheme()
        initializeWorkers()
        AppSignatureHelper(this)
    }

    private fun initializeTheme(){

    }

    private fun initializeWorkers(){
        MyWorkManager.initializeAppWorkers(applicationContext)
        MyWorkManager.synchronizeContactsWithServer(applicationContext)
    }


}