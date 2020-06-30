package ng.novacore.sleezchat.adapters.tabLayout

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ng.novacore.sleezchat.ui.chat.call.CallFragment
import ng.novacore.sleezchat.ui.chat.camera.CameraFragment
import ng.novacore.sleezchat.ui.chat.chatList.ChatListFragment
import ng.novacore.sleezchat.ui.chat.status.StatusFragment
import ng.novacore.sleezchat.utils.Constants

class ChatContainerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){

    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        Constants.CHAT_CONTAINER_TO_CAMERA to { CameraFragment() },
        Constants.CHAT_CONTAINER_TO_CHAT to { ChatListFragment() },
        Constants.CHAT_CONTAINER_TO_STATUS to {  StatusFragment() },
        Constants.CHAT_CONTAINER_TO_CALL to { CallFragment() }
    )

    override fun getItemCount(): Int = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }

}