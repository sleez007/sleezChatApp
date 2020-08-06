package ng.novacore.sleezchat.activities.verification

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import dagger.hilt.android.AndroidEntryPoint
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.activities.MainActivity
import ng.novacore.sleezchat.databinding.ActivityVerificationBinding
import ng.novacore.sleezchat.utils.SnackBarUtil
import ng.novacore.sleezchat.utils.WindowsUtil
import timber.log.Timber


@Suppress("DEPRECATION")
@AndroidEntryPoint
class VerificationActivity : AppCompatActivity() {
    companion object{
        var mThis:VerificationActivity?= null
    }
    private lateinit var navController: NavController
    var bind: ActivityVerificationBinding? = null

   val viewModel : VerificationViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        WindowsUtil.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.brand_primary))
        super.onCreate(savedInstanceState)
       bind = DataBindingUtil.setContentView(this,R.layout.activity_verification )
        navController = findNavController(R.id.nav_verification_host_fragment)
        bind?.vm = viewModel
        mThis= this
        bind?.lifecycleOwner = this
        destinationChangeListener()
        liveDataEventListeners()
    }

    /**
     * @desc Setup LiveData streams listeners
     */
    private fun liveDataEventListeners(){
        viewModel.successMsg.observe(this, Observer {
            it?.getContentIfNotHandled()?.let {msg->
                bind?.let {
                    SnackBarUtil.successMsg(it.container,msg,this)
                }
            }
        })
        viewModel.warningMsg.observe(this, Observer {
            it?.getContentIfNotHandled()?.let {msg->
                bind?.let {
                    SnackBarUtil.warningMsg(it.container,msg,this)
                }
            }
        })
        viewModel.errorMsg.observe(this, Observer {
            it?.getContentIfNotHandled()?.let {msg->
                bind?.let {
                    SnackBarUtil.errorMsg(it.container,msg,null,this)
                }
            }
        })
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

    /**
     * Destination change listeners
     */
    private fun destinationChangeListener(){
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


    override fun onDestroy() {
        mThis = null
        super.onDestroy()
    }


    /**
     * Retrieve OTP MESSAGE listener
     */
    fun retrieveIncomingSms(){
        val client: SmsRetrieverClient =  SmsRetriever.getClient(this)

        // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
        // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
        // action SmsRetriever#SMS_RETRIEVED_ACTION.
        val task = client.startSmsRetriever()


        // Listen for success/failure of the start Task. If in a background thread, this
        // can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener {
            //registered
            // Successfully started retriever, expect broadcast intent
            viewModel.stopTimer()
            viewModel.startTimer()
        }

        task.addOnFailureListener {
            Timber.i("failed to add a listener")
            // Failed to start retriever, inspect Exception for more details
        }
    }




}
