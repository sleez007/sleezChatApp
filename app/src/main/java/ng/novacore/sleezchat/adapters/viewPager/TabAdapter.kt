package ng.novacore.sleezchat.adapters.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import ng.novacore.sleezchat.ui.chat.call.CallFragment
import ng.novacore.sleezchat.ui.chat.camera.CameraFragment
import ng.novacore.sleezchat.ui.chat.chatList.ChatListFragment
import ng.novacore.sleezchat.ui.chat.status.StatusFragment

class TabAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm,  BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
    val NUM_ITEMS = 4
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> CameraFragment()
            1 -> ChatListFragment()
            2 -> StatusFragment()
            3 -> CallFragment()
            else -> ChatListFragment()
        }
    }

    override fun getCount(): Int = NUM_ITEMS

}