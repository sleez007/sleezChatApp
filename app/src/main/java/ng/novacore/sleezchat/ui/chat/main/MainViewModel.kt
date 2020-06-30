package ng.novacore.sleezchat.ui.chat.main

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ng.novacore.sleezchat.repository.AppRepositoryInterface

class MainViewModel  @ViewModelInject constructor(private val repository: AppRepositoryInterface, @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel(){
    private val _currentPage : MutableLiveData<Int> = MutableLiveData(0)
    val currentPage: LiveData<Int> = _currentPage

    fun setPageIndex(page : Int){
        _currentPage.value = page
    }
}
