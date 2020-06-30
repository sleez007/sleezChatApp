package ng.novacore.sleezchat.ui.verification.phoneVerification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import dagger.hilt.android.AndroidEntryPoint

import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.activities.verification.VerificationActivity
import ng.novacore.sleezchat.activities.verification.VerificationViewModel
import timber.log.Timber

@AndroidEntryPoint
class PhoneVerificationFragment : Fragment() {

    val viewModel: VerificationViewModel by navGraphViewModels(R.id.verification_navigation) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_phone_verification, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        Timber.i(viewModel.currentPage.toString()+"bjbjvivi")

        (activity as VerificationActivity).viewModel.currentFrag.observe(viewLifecycleOwner, Observer {

            it?.getContentIfNotHandled()?.let {
                if(it == 1){
                    val action = PhoneVerificationFragmentDirections.actionNavPhoneToNavOtp()
                    findNavController().navigate(action)
                }
            }
        })

    }

}
