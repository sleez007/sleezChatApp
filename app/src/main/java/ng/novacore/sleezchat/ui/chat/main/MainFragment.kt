package ng.novacore.sleezchat.ui.chat.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.adapters.tabLayout.ChatContainerAdapter
import ng.novacore.sleezchat.databinding.FragmentMainBinding
import ng.novacore.sleezchat.utils.Constants
import timber.log.Timber

@AndroidEntryPoint
class MainFragment : Fragment() {

    var binding: FragmentMainBinding? = null

    val viewModel : MainViewModel by viewModels()

    var tabMed : TabLayoutMediator? = null

    private val listener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            Timber.i(position.toString())
            viewModel.setPageIndex(position)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding?.apply {
            viewPager.adapter = ChatContainerAdapter(this@MainFragment)
            tabMed = TabLayoutMediator(tabs, viewPager){tab, position ->
                setTabs(tab, position)
            }
            tabMed?.attach()

        }
        return binding?.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            viewPager.registerOnPageChangeCallback(listener)
        }
    }


    override fun onDestroyView() {
        tabMed?.detach()
        tabMed = null
        binding?.viewPager?.unregisterOnPageChangeCallback(listener)
        binding?.viewPager?.adapter = null
        binding = null
        super.onDestroyView()
    }

    private fun setTabs(tab: TabLayout.Tab, position: Int){
        when(position){
            Constants.CHAT_CONTAINER_TO_CAMERA->{
                tab.setIcon(R.drawable.ic_camera_alt_black_24dp)
            }
            Constants.CHAT_CONTAINER_TO_CHAT->{
                tab.text = getString(R.string.chat)
            }
            Constants.CHAT_CONTAINER_TO_STATUS->{
                tab.text = getString(R.string.status)
            }
            Constants.CHAT_CONTAINER_TO_CALL->{
                tab.text = getString(R.string.callm)
            }
        }
    }

}
