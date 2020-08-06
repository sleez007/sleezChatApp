package ng.novacore.sleezchat.ui.chat.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.adapters.viewPager.TabAdapter
import ng.novacore.sleezchat.databinding.FragmentMainBinding
import ng.novacore.sleezchat.internals.enums.FabPage
import timber.log.Timber

@AndroidEntryPoint
class MainFragment : Fragment() {

    var binding: FragmentMainBinding? = null

    val viewModel : MainViewModel by viewModels()

    private var tabMed : TabLayoutMediator? = null

    private val pageChangeListener  = object: ViewPager.OnPageChangeListener{
        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageSelected(position: Int) {
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
            viewPager.offscreenPageLimit = 3
            tabs.setupWithViewPager(viewPager)
            tabs.setTabGravity(TabLayout.GRAVITY_FILL)
            tabs.setTabMode(TabLayout.MODE_FIXED)
            viewPager.currentItem = 1
            viewPager.bindViews(appBarLayout)
            viewPager.initTransformer(savedInstanceState, false)
            viewPager.addOnPageChangeListener(pageChangeListener)
            setUpTabs()
            setupTab()

        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }
        setUpListeners()
    }


    override fun onDestroyView() {
        tabMed?.detach()
        tabMed = null
        binding?.viewPager?.adapter = null
        binding?.viewPager?.removeOnPageChangeListener(pageChangeListener)
        binding = null
        super.onDestroyView()
    }

    private fun setUpListeners(){
        viewModel.fabHandler.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let {
                when(it){
                    FabPage.TO_CONTACTS->{}
                    FabPage.TO_CALL->{}
                    FabPage.TO_CAMERA->{}
                }
            }
        })
    }

    /**
     * @desc dynamically add tabs
     */
   private fun setUpTabs(){
        binding?.apply{
            tabs.getTabAt(0)?.setIcon(R.drawable.ic_camera_alt_black_24dp)
            tabs.getTabAt(1)?.text ="CHATS"
            tabs.getTabAt(2)?.text ="STATUS"
            tabs.getTabAt(3)?.text ="CALLS"
        }
   }

    /**
     * SHRINK THE CAMERA TAB
     */
    private fun setupTab() {
        val tab = (binding?.tabs?.getChildAt(0) as LinearLayout).getChildAt(0) as LinearLayout
        val params = tab.layoutParams as LinearLayout.LayoutParams
        params.weight = 0f
        params.width = LinearLayout.LayoutParams.WRAP_CONTENT
        tab.layoutParams = params
    }



}



