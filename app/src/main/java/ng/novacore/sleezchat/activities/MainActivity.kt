package ng.novacore.sleezchat.activities


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import ng.novacore.sleezchat.R

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
       // WindowsUtil.changeStatusBarColor(this,ContextCompat.getColor(this, R.color.dark_green))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }











}
