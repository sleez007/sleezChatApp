package ng.novacore.sleezchat.activities.verification

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.scopes.ActivityScoped
import ng.novacore.sleezchat.repository.AppRepositoryInterface
import ng.novacore.sleezchat.utils.Event
import timber.log.Timber

class VerificationViewModel @ViewModelInject constructor(private val repository: AppRepositoryInterface,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val _currentFrag = MutableLiveData<Event<Int>>()
    val currentFrag: LiveData<Event<Int>> =_currentFrag
    var currentPage: Int = 0
    var navigateToHome = MutableLiveData<Event<Boolean>>()

    init {
        Timber.i("Hil mary")
    }



    fun clickHandler(){
        if(currentPage >= 2){
            navigateToHome.value = Event(true)
        }else{
            _currentFrag.value = Event(++currentPage)
        }


    }

}