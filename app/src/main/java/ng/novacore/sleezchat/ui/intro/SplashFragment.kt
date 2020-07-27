package ng.novacore.sleezchat.ui.intro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.activities.MainActivity
import ng.novacore.sleezchat.activities.intro.IntroActivity
import ng.novacore.sleezchat.activities.intro.IntroViewModel
import ng.novacore.sleezchat.activities.verification.VerificationActivity
import ng.novacore.sleezchat.internals.enums.NavEnum

@AndroidEntryPoint
class SplashFragment : Fragment() {

    lateinit var viewModel: IntroViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = (activity as IntroActivity).viewModel
        viewModel.toScreen.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let {
                when(it){
                    NavEnum.TO_WIZARD->{
                        val action  = SplashFragmentDirections.actionNavSplashToNavWiz()
                        findNavController().navigate(action)
                    }
                    NavEnum.TO_MAIN->{
                        switchActivity( MainActivity::class.java)
                    }
                    NavEnum.TO_PHONE_NUM ->{
                        switchActivity( VerificationActivity::class.java)
                    }
                    NavEnum.TO_PROFILE->{
                        switchActivity( VerificationActivity::class.java)
                    }
                }
            }
        })
    }

    private fun switchActivity(destination: Class<*>) {
        Intent(requireActivity(),destination ).apply {
            startActivity(this)
            requireActivity().finish()
        }
    }
}