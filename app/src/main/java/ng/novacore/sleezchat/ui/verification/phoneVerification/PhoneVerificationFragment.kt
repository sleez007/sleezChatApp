package ng.novacore.sleezchat.ui.verification.phoneVerification

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import dagger.hilt.android.AndroidEntryPoint

import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.activities.MainActivity
import ng.novacore.sleezchat.activities.verification.VerificationActivity
import ng.novacore.sleezchat.activities.verification.VerificationViewModel
import ng.novacore.sleezchat.databinding.FragmentPhoneVerificationBinding
import ng.novacore.sleezchat.internals.enums.NavEnum
import ng.novacore.sleezchat.internals.enums.VerificationEnum
import ng.novacore.sleezchat.ui.intro.SplashFragmentDirections
import ng.novacore.sleezchat.utils.Constants.FCM_PHONE_AUTH_RESULT
import timber.log.Timber

@AndroidEntryPoint
@Suppress("DEPRECATION")
class PhoneVerificationFragment : Fragment(),GoogleApiClient.OnConnectionFailedListener {
    var binding: FragmentPhoneVerificationBinding? = null

   lateinit var viewModel: VerificationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhoneVerificationBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = (activity as VerificationActivity).viewModel
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            ccp.registerCarrierNumberEditText(editText)
            editText.setOnFocusChangeListener { _, isFocused ->
                if(isFocused){
                    if(!(activity as VerificationActivity).viewModel.hasRequestedPhoneNumber){
                        (activity as VerificationActivity).viewModel.hasRequestedPhoneNumber = true
                        requestPhoneNumber()
                    }
                }
            }
            //Toast.makeText(requireContext(),ccp.selectedCountryNameCode,Toast.LENGTH_LONG).show()
        }
        viewModel.currentFrag.observe(viewLifecycleOwner, Observer {

            it?.getContentIfNotHandled()?.let {
                if(it == 1){
                    binding?.let {
                        viewModel.validatePhoneNumber(it.ccp.fullNumberWithPlus, it.ccp.selectedCountryNameCode,it.ccp.selectedCountryEnglishName)
                    }
                }
            }

        })
        viewModel.navigate.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let {
                when(it){
                    VerificationEnum.TO_OTP_FRAG->{
                        val action = PhoneVerificationFragmentDirections.actionNavPhoneToNavOtp(viewModel.telNo)
                        findNavController().navigate(action)
                    }
                }
            }
        })
        viewModel.setUpReceiver.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let{
                if(it) (activity as VerificationActivity).retrieveIncomingSms()
            }
        })
        viewModel.toScreen.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let {
                when(it){
                    NavEnum.TO_PROFILE->{
                        val action = PhoneVerificationFragmentDirections.actionGlobalNavProfileInfo()
                        findNavController().navigate(action)
                    }
                    else->{
                        //DO Nothing here
                    }
                }
            }
        })

    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun requestPhoneNumber(){
        val mGoogleApiClient = GoogleApiClient.Builder(requireContext()).enableAutoManage(requireActivity(), this).addApi(
            Auth.CREDENTIALS_API).build()
        val hintRequest : HintRequest = HintRequest.Builder().setPhoneNumberIdentifierSupported(true).build()
        val intent: PendingIntent = Auth.CredentialsApi.getHintPickerIntent(mGoogleApiClient, hintRequest)
        startIntentSenderForResult(intent.intentSender,
            FCM_PHONE_AUTH_RESULT, null, 0, 0, 0,null)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FCM_PHONE_AUTH_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                val credential: Credential? = data!!.getParcelableExtra(Credential.EXTRA_KEY)
               credential?.let {
                   if(it.id.contains("234")){
                      val value = it.id.replace("\\s+","").replaceFirst("+234","")
                       binding?.editText?.setText(value)
                   }else{
                       binding?.editText?.setText(it.id)
                   }
               }
            }
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

}
