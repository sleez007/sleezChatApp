package ng.novacore.sleezchat.utils

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity


object WindowsUtil {
    /**
     * @desc change status bar color
     * @param context this is the context of the activity which you wish to change it's look
     * @param color this is the actual color you want to be applied to the status bar
     */
    fun changeStatusBarColor(context: Context, color: Int) {
        if (Build.VERSION.SDK_INT >= 21) {
            val window: Window = (context as AppCompatActivity).window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
        }
    }

    /**
     * @desc allow the activity takeup the full screen and draw behind the status bar
     */
    fun fullScreenUi(context: Context) {
        (context as AppCompatActivity).window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            decorView.systemUiVisibility =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
           // navigationBarColor = Color.BLACK
        }
    }

    /**
     * Hide keyboard
     */
    fun hideKeyboard(context: Context, view: View) {
        val inputMethodManager =
            context.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}