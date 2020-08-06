package ng.novacore.sleezchat.ui.chat.contacts

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ng.novacore.sleezchat.repository.AppRepository

class FragmentContactsViewModel @ViewModelInject constructor(private val repository: AppRepository, @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {

}