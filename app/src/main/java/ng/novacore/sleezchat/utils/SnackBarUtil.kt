package ng.novacore.sleezchat.utils

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.internals.interfaces.RetryRequest

object SnackBarUtil {

    fun errorMsg(parentLayout: View, msg: String, executable : RetryRequest?, context: Context, duration: Int = Snackbar.LENGTH_LONG){
        if(executable!= null){
            Snackbar.make(parentLayout, msg, duration).setAction(context.getString(R.string.retry)){
                executable.retry()
            }.setBackgroundTint(ContextCompat.getColor(context, R.color.error_snack)).show()
        }else{
            Snackbar.make(parentLayout, msg, duration).setBackgroundTint(ContextCompat.getColor(context, R.color.error_snack)).show()
        }
    }

    fun warningMsg(parentLayout: View, msg: String, context: Context){
        Snackbar.make(parentLayout,msg,Snackbar.LENGTH_LONG).setBackgroundTint(ContextCompat.getColor(context, R.color.warning_snack)).show()
    }

    fun successMsg(parentLayout: View, msg: String, context: Context){
        Snackbar.make(parentLayout,msg,Snackbar.LENGTH_LONG).setBackgroundTint(ContextCompat.getColor(context, R.color.success_snack)).show()
    }
}