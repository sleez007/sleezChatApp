package ng.novacore.sleezchat.receivers

import android.R.attr.data
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import ng.novacore.sleezchat.activities.verification.VerificationActivity
import timber.log.Timber
import java.util.regex.Matcher
import java.util.regex.Pattern


class MySMSBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
       if(SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action){
           val extras = intent.extras
           val status: Status? = extras!![SmsRetriever.EXTRA_STATUS] as Status?

           when(status?.statusCode){
               CommonStatusCodes.SUCCESS ->{
                   val message = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String
                   extractOtp(context,message)

               }
               CommonStatusCodes.TIMEOUT ->{
                   // Waiting for SMS timed out (5 minutes)
                   // Handle the error ...
               }
           }
       }
    }

    private fun extractOtp(context: Context?,msg:String){
        val  p = Pattern.compile("(\\d{4})")
        val matcher: Matcher = p.matcher(msg)
        if(matcher.find()){
            val pin = matcher.group(0)
            VerificationActivity.mThis?.let {
                Toast.makeText(context, pin, Toast.LENGTH_LONG).show()
                Timber.i(pin)
                it.viewModel.otp_code.value = pin
            }
        }
    }
}