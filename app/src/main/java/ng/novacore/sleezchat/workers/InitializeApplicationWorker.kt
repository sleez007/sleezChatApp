package ng.novacore.sleezchat.workers

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import ng.novacore.sleezchat.application.SocketConstants
import ng.novacore.sleezchat.helper.SharedPrefHelper
import ng.novacore.sleezchat.managers.SocketConnection
import timber.log.Timber
import java.lang.Exception

class InitializeApplicationWorker @WorkerInject constructor(val sharedPrefHelper: SharedPrefHelper, val socketManager: SocketConnection, @Assisted appContext: Context, @Assisted workerParams: WorkerParameters): CoroutineWorker(appContext,workerParams) {


    override suspend fun doWork(): Result {
         return withContext(Dispatchers.IO){
            val jobs = listOf(async {newUserEmitter() }, async { syncMyInfo() })
            val bb =jobs.awaitAll()
             Timber.i(bb.toString())
            if( bb.indexOf(false)>=0) Result.retry() else Result.success()
        }

    }


    /**
     * @desc THIS METHOD EMITS OFF AN EVENT TO THE SERVER INFORMING IT OF A NEW USER CONNECTION
     */
    private suspend fun newUserEmitter(): Boolean{
        return try{
            val jsonObj : JsonObject = JsonObject()
            jsonObj.addProperty("userId", sharedPrefHelper.getUserID())
            Timber.i(jsonObj.toString())
            socketManager.socket?.emit(SocketConstants.SOCKET_NEW_USER,jsonObj)
            true
        }catch (ex: Exception){
            ex.printStackTrace()
            false
        }
    }

    /**
     * This method should retrieve my personal info from the server.
     * The method looks a bit redundant at the moment
     */

    private suspend fun syncMyInfo(): Boolean{
        return try {
            //TODO WRITE RETRIEVAL LOGIC HERE
            true
        }catch (ex: Exception){
            ex.printStackTrace()
            false
        }
    }
}