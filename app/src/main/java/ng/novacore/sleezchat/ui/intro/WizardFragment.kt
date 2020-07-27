package ng.novacore.sleezchat.ui.intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.activities.intro.IntroActivity
import ng.novacore.sleezchat.activities.intro.IntroViewModel
import ng.novacore.sleezchat.adapters.viewPager.WizardAdapter
import ng.novacore.sleezchat.databinding.FragmentWizardBinding

@AndroidEntryPoint
class WizardFragment : Fragment() {
    lateinit var viewModel: IntroViewModel
    var binding: FragmentWizardBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWizardBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
        }
        viewModel = (activity as IntroActivity).viewModel
        wireViewPager()
    }

    private fun wireViewPager(){
        val adapter = WizardAdapter(requireContext())
        binding?.let {
            it.viewPager2.adapter = adapter
            it.dotsIndicator.setViewPager2(it.viewPager2)
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }



}