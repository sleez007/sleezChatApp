package ng.novacore.sleezchat.activities.intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.utils.WindowsUtil

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {

    val viewModel : IntroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowsUtil.fullScreenUi(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
    }
}
