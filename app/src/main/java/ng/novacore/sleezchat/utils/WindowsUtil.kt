package ng.novacore.sleezchat.utils

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.Window
import android.view.WindowManager
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
}