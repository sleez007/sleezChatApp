package ng.novacore.sleezchat.ui.chat.camera

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ng.novacore.sleezchat.repository.AppRepositoryInterface
import timber.log.Timber

class CameraViewModel @ViewModelInject constructor(private val repository: AppRepositoryInterface, @Assisted private val savedStateHandle: SavedStateHandle
)  : ViewModel() {
    // TODO: Implement the ViewModel

    init {
        Timber.i("he called")
    }
}
