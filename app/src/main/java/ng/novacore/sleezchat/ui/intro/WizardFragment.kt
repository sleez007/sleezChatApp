package ng.novacore.sleezchat.ui.intro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.activities.MainActivity
import ng.novacore.sleezchat.activities.intro.IntroActivity
import ng.novacore.sleezchat.activities.intro.IntroViewModel
import ng.novacore.sleezchat.activities.verification.VerificationActivity
import ng.novacore.sleezchat.adapters.viewPager.WizardAdapter
import ng.novacore.sleezchat.databinding.FragmentWizardBinding
import ng.novacore.sleezchat.internals.enums.NavEnum
import timber.log.Timber
import java.lang.Exception

@AndroidEntryPoint
class WizardFragment : Fragment() {
    val listener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            viewModel.setWriteUp(position)
        }
    }

    lateinit var viewModel: IntroViewModel
    var binding: FragmentWizardBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWizardBinding.inflate(inflater, container, false)
        viewModel = (activity as IntroActivity).viewModel
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }
        viewModel = (activity as IntroActivity).viewModel
        viewModel.toScreen.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let {
                when(it){

                    NavEnum.TO_PHONE_NUM ->{
                        switchActivity( VerificationActivity::class.java)
                    }
                    else->{
                        //Leave this part empty
                    }
                }
            }
        })
        wireViewPager()
    }

    private fun switchActivity(destination: Class<*>) {
        Intent(requireActivity(),destination ).apply {
            startActivity(this)
            requireActivity().finish()
        }
    }

    private fun wireViewPager(){
        val adapter = WizardAdapter(requireContext())
        binding?.let {
            it.viewPager2.adapter = adapter
            it.viewPager2.registerOnPageChangeCallback(listener)
            it.dotsIndicator.setViewPager2(it.viewPager2)
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        try{
            binding?.viewPager2?.unregisterOnPageChangeCallback(listener)
        }catch (ex: Exception){
            println(ex.message)
        }
        super.onDestroy()
    }



}