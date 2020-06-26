package ng.novacore.sleezchat.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DeviceBootReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action?.equals(Intent.ACTION_BOOT_COMPLETED) == true){

            //try uploading all messages that i sent offline
            //try uploading my stories as well
        }
    }
}