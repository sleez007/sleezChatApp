package ng.novacore.sleezchat.utils

import android.content.Context
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity


object WindowsUtil {
    fun changeStatusBarColor(context: Context, color: Int) {
        if (Build.VERSION.SDK_INT >= 21) {
            val window: Window = (context as AppCompatActivity).window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
        }
    }

    /**
     * Hide keyboard
     */
    fun hideKeyboard(context: Context, view: View) {
        val inputMethodManager = context.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}