package ng.novacore.sleezchat.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status


class MySMSBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
       if(SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action){
           val extras = intent.extras
           val status: Status? = extras!![SmsRetriever.EXTRA_STATUS] as Status?

           when(status?.statusCode){
               CommonStatusCodes.SUCCESS ->{

               }
               CommonStatusCodes.TIMEOUT ->{

               }
           }
       }
    }
}