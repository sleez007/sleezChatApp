package ng.novacore.sleezchat.application

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import ng.novacore.sleezchat.BuildConfig
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.helper.ThemeHelper
import ng.novacore.sleezchat.managers.MyWorkManager
import ng.novacore.sleezchat.utils.Constants
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
        //AppSignatureHelper(this)
    }

    private fun initializeTheme(){
        val sharedPref: SharedPreferences = getSharedPreferences(BuildConfig.PREF_NAME, Context.MODE_PRIVATE)
        val theme = sharedPref.getString(resources.getString(R.string.preference_key_theme_option), Constants.THEME_DEFAULT)
        theme?.let {
            ThemeHelper.applyTheme(it)
        }
    }

    private fun initializeWorkers(){
        val sharedPref: SharedPreferences = getSharedPreferences(BuildConfig.PREF_NAME, Context.MODE_PRIVATE)
        if(sharedPref.getString(Constants.TOKEN_LOOKUP,null)!= null){
            MyWorkManager.initializeAppWorkers(applicationContext)
            MyWorkManager.synchronizeContactsWithServerPeriodic(applicationContext)
            MyWorkManager.synchronizeContactsWithServerAsap(applicationContext)
        }
    }


}