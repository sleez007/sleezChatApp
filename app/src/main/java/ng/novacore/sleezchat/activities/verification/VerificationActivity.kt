package ng.novacore.sleezchat.activities.verification

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.activities.MainActivity
import ng.novacore.sleezchat.databinding.ActivityVerificationBinding
import ng.novacore.sleezchat.utils.WindowsUtil

@Suppress("DEPRECATION")
@AndroidEntryPoint
class VerificationActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    private lateinit var navController: NavController
    var bind: ActivityVerificationBinding? = null


   val viewModel : VerificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowsUtil.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.dark_green))
        super.onCreate(savedInstanceState)
       bind = DataBindingUtil.setContentView(this,R.layout.activity_verification )
        navController = findNavController(R.id.nav_verification_host_fragment)
        bind?.vm = viewModel
        bind?.lifecycleOwner = this
        destinationChangeListener()
        eventObservers()
    }

    fun destinationChangeListener(){
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id){
                R.id.nav_phone ->{
                    viewModel.currentPage = 0
                    switchCustomDots(0)
                }
                R.id.nav_otp ->{
                    viewModel.currentPage = 1
                    switchCustomDots(1)
                }
                R.id.nav_profile_info->{
                    viewModel.currentPage = 2
                    switchCustomDots(2)
                }
            }
        }
    }

    private fun switchCustomDots(currFragIndex: Int){
        when(currFragIndex){
            0 ->{
                bind?.let {
                    it.view2.setBackgroundColor(ContextCompat.getColor(this,R.color.dash_line_active))
                    it.view.setBackgroundColor(ContextCompat.getColor(this,R.color.dash_line_in_active))
                    it.view3.setBackgroundColor(ContextCompat.getColor(this,R.color.dash_line_in_active))
                    it.floatingActionButton.setImageResource(R.drawable.ic_arrow_forward_black_24dp)
                }
            }
            1 ->{
                bind?.let {
                    it.view2.setBackgroundColor(ContextCompat.getColor(this,R.color.dash_line_in_active))
                    it.view.setBackgroundColor(ContextCompat.getColor(this,R.color.dash_line_active))
                    it.view3.setBackgroundColor(ContextCompat.getColor(this,R.color.dash_line_in_active))
                    it.floatingActionButton.setImageResource(R.drawable.ic_arrow_forward_black_24dp)
                }
            }
            else->{
                bind?.let {
                    it.view2.setBackgroundColor(ContextCompat.getColor(this,R.color.dash_line_in_active))
                    it.view.setBackgroundColor(ContextCompat.getColor(this,R.color.dash_line_in_active))
                    it.view3.setBackgroundColor(ContextCompat.getColor(this,R.color.dash_line_active))
                    it.floatingActionButton.setImageResource(R.drawable.ic_check_black_24dp)
                }
            }
        }
    }

    private fun eventObservers(){
        viewModel.navigateToHome.observe(this, Observer {
            it?.getContentIfNotHandled()?.let {
                if(it){
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 20) {
            if (resultCode == Activity.RESULT_OK) {
                val credential: Credential? = data!!.getParcelableExtra(Credential.EXTRA_KEY)
                Toast.makeText(this, credential!!.id, Toast.LENGTH_LONG).show()
                // credential.getId();  <-- will need to process phone number string
            }
        }
    }

    private fun requestPhoneNumber(){

        val mGoogleApiClient = GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(
            Auth.CREDENTIALS_API).build()


        val hintRequest : HintRequest = HintRequest.Builder().setPhoneNumberIdentifierSupported(true).build()
        val intent: PendingIntent = Auth.CredentialsApi.getHintPickerIntent(mGoogleApiClient, hintRequest)
        startIntentSenderForResult(intent.intentSender,
            20, null, 0, 0, 0);
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }


}
