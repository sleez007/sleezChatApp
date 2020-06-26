package ng.novacore.sleezchat.services


import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
       //Store New Firebase Token to cloud server
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
       //TODO("CHECK IF APP IS FOREGROUND AND THEN KNOW HOW TO HANDLE INCOMING NOTIFICATION")
        try {

            // Check if message contains a data payload
            if (remoteMessage.data.isNotEmpty()) {
                Timber.i("Message data payload: ${remoteMessage.data}")
                extractNotificationBody(remoteMessage.data)

                if (/* Check if data needs to be processed by long running job */ true) {
                    // For long-running tasks (10 seconds or more) use WorkManager.
                   // scheduleJob()
                } else {
                    // Handle message within 10 seconds
                    //handleNow()
                }
            }

            // Check if message contains a notification payload.
            remoteMessage.notification?.let {

                Timber.i("Notification body ${it.body}")
            }

        }catch (ex : Exception){
            ex.printStackTrace()
        }

    }

    private fun extractNotificationBody(data: Map<String?, String?>) {
        data.get("topics")?.let {
            if(it == applicationContext.packageName){
                //Trigger socket connection again to see if it's responding
            }
        }
    }
}