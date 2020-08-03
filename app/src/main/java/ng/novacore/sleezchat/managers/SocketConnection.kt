package ng.novacore.sleezchat.managers

import android.content.Context
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.JsonObject
import dagger.hilt.android.qualifiers.ApplicationContext
import ng.novacore.sleezchat.application.SocketConstants
import ng.novacore.sleezchat.helper.SharedPrefHelper
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SocketConnection @Inject constructor(val socket: Socket, @ApplicationContext context: Context, val sharedPrefHelper: SharedPrefHelper) {

    init {
        connectSocket()
    }
    /**
     *  @desc : CONNECT TO THE REALTIME SOCKET AND ALSO REGISTER EVENT LISTENERS AND EXECUTION
     */
    fun connectSocket(){
        socket.on(SocketConstants.SOCKET_EVENT_CONNECT) {
            Timber.i("Retrying  connection")
            try{
                val jsonObj : JsonObject = JsonObject()
                jsonObj.addProperty("connected", true)
                jsonObj.addProperty("connectedID", sharedPrefHelper.getUserID() )
                jsonObj.addProperty("socketId", socket?.id())
                socket.emit(SocketConstants.SOCKET_USER_CONNECTED, jsonObj)
            }catch (ex: Exception){
                ex.printStackTrace()
            }
        }.on(SocketConstants.SOCKET_USER_DISCONNECT){
            Timber.i("I have lost connection to the server")
        }
        socket.connect()
        emitUserIsOnline()
    }

    private fun emitUserIsOnline(){
        try{
            val jsonObj : JsonObject = JsonObject()
            jsonObj.addProperty("connected", true)
            jsonObj.addProperty("connectedID", sharedPrefHelper.getUserID() )
            socket.emit(SocketConstants.SOCKET_USER_CONNECTED, jsonObj)
        }catch (ex: Exception){
            ex.printStackTrace()
        }

    }



}