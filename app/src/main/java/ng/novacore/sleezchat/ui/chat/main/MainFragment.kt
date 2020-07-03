package ng.novacore.sleezchat.ui.chat.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.databinding.FragmentMainBinding
import ng.novacore.sleezchat.ui.chat.call.CallFragment
import ng.novacore.sleezchat.ui.chat.camera.CameraFragment
import ng.novacore.sleezchat.ui.chat.chatList.ChatListFragment
import ng.novacore.sleezchat.ui.chat.status.StatusFragment
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
            when(position){
                0->{

                }
                1->{

                }
                2->{

                }
                3->{

                }
            }

            Timber.i(position.toString())
            viewModel.setPageIndex(position)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        initializeView(savedInstanceState)
        return binding?.root
    }

    private fun initializeView(savedInstanceState: Bundle?) {
        binding?.apply {
            viewPager.adapter = TabAdapter(requireActivity().supportFragmentManager)
            viewPager.setOffscreenPageLimit(3)
            tabs.setupWithViewPager(viewPager)
            tabs.setTabGravity(TabLayout.GRAVITY_FILL)
            tabs.setTabMode(TabLayout.MODE_FIXED)
            viewPager.currentItem = 1
            viewPager.bindViews(appBarLayout)
            viewPager.initTransformer(savedInstanceState, false)
            setUpTabs()
            setupTab()

        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
           // viewPager.registerOnPageChangeCallback(listener)
        }
    }


    override fun onDestroyView() {
        tabMed?.detach()
        tabMed = null
        //binding?.viewPager?.unregisterOnPageChangeCallback(listener)
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

   fun setUpTabs(){
        binding?.apply{
            //tabs.removeAllTabs()
            tabs.getTabAt(0)?.setIcon(R.drawable.ic_camera_alt_black_24dp)
            tabs.getTabAt(1)?.text ="CHATS"
            tabs.getTabAt(2)?.text ="STATUS"
            tabs.getTabAt(3)?.text ="CALLS"
            //tabs.getTabAt(1)?.customView=tabItem2
        }
   }

    private fun setupTab() {
        val tab = (binding?.tabs?.getChildAt(0) as LinearLayout).getChildAt(0) as LinearLayout
        val params = tab.layoutParams as LinearLayout.LayoutParams
        params.weight = 0f
        params.width = LinearLayout.LayoutParams.WRAP_CONTENT
        tab.layoutParams = params
    }

}


class TabAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm,  BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> CameraFragment()
            1 -> ChatListFragment()
            2 ->StatusFragment()
            3 -> CallFragment()
            else -> ChatListFragment()
        }
    }

    override fun getCount(): Int = 4

}
