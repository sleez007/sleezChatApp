package ng.novacore.sleezchat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import ng.novacore.sleezchat.R

@AndroidEntryPoint
class WizardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wizard)
        //private val exampleViewModel: ExampleViewModel by viewModels()
    }
}
