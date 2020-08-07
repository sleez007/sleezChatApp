package ng.novacore.sleezchat.ui.chat.chatList

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ng.novacore.sleezchat.repository.AppRepository
import timber.log.Timber

class ChatListViewModel @ViewModelInject constructor(private val repository: AppRepository, @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
   init {
       Timber.i("I don mad o!")
   }
}
