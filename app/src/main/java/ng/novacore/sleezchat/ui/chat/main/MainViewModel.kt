package ng.novacore.sleezchat.ui.chat.main

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ng.novacore.sleezchat.internals.enums.FabPage
import ng.novacore.sleezchat.repository.AppRepository
import ng.novacore.sleezchat.utils.Event

class MainViewModel  @ViewModelInject constructor(private val repository: AppRepository, @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel(){
    private val _currentPage : MutableLiveData<Int> = MutableLiveData(1)
    val currentPage: LiveData<Int> = _currentPage
    val fabHandler = MutableLiveData<Event<FabPage>>()

    fun setPageIndex(page : Int){
        _currentPage.value = page
    }

    fun fabClick(position: Int){
        when(position){
            1->fabHandler.value
        }
    }
}
