package ng.novacore.sleezchat.ui.verification.otpVerification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import dagger.hilt.android.AndroidEntryPoint

import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.activities.verification.VerificationActivity
import ng.novacore.sleezchat.activities.verification.VerificationViewModel
import ng.novacore.sleezchat.databinding.FragmentOtpBinding
import ng.novacore.sleezchat.internals.enums.VerificationEnum
import ng.novacore.sleezchat.ui.verification.phoneVerification.PhoneVerificationFragmentDirections
import timber.log.Timber


@AndroidEntryPoint
class OtpFragment : Fragment() {

    lateinit var viewModel: VerificationViewModel
//    val viewModel: VerificationViewModel by navGraphViewModels(R.id.verification_navigation) {
//        defaultViewModelProviderFactory
//    }

    val args : OtpFragmentArgs by navArgs()

    var binding: FragmentOtpBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtpBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = (activity as VerificationActivity).viewModel
        viewModel.currentFrag.observe(viewLifecycleOwner, Observer {

            it?.getContentIfNotHandled()?.let {
                if(it == 2){
                    viewModel.verifyOtp()
                }
            }
        })
        viewModel.navigate.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let {
                when(it){
                    VerificationEnum.TO_PROFILE_FRAG->{
                        val action = OtpFragmentDirections.actionGlobalNavProfileInfo()
                        findNavController().navigate(action)
                    }
                }

            }
        })
        wireUpBinding()
    }

    private fun wireUpBinding(){
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            textView.text = getString(R.string.waiting_to_automatically_detect_an_sms_sent,args.telNo)
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}
