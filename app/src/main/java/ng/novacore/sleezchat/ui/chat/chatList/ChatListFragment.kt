package ng.novacore.sleezchat.ui.chat.chatList

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.databinding.FragmentChatListBinding
import ng.novacore.sleezchat.ui.chat.main.MainViewModel
import timber.log.Timber

@AndroidEntryPoint
class ChatListFragment : Fragment() {

    var binding : FragmentChatListBinding? = null

    val viewModel : ChatListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.i("I don mad create o!")
        viewModel
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        Timber.i("I have been destroyed!")
        super.onDestroy()
    }

}
