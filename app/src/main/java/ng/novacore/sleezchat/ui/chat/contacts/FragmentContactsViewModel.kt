package ng.novacore.sleezchat.ui.chat.contacts

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagedList
import ng.novacore.sleezchat.db.entity.UsersEntity
import ng.novacore.sleezchat.internals.interfaces.ClickContract
import ng.novacore.sleezchat.internals.interfaces.RetryRequest
import ng.novacore.sleezchat.repository.AppRepository
import ng.novacore.sleezchat.utils.Event
import timber.log.Timber

class FragmentContactsViewModel @ViewModelInject constructor(private val repository: AppRepository, @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel(), RetryRequest,ClickContract<UsersEntity> {
    val loading : MutableLiveData<Boolean> =  MutableLiveData(false)
    val isError : MutableLiveData<Boolean> = MutableLiveData(false)
    val myContacts :LiveData<PagedList<UsersEntity>> = repository.getMyContacts()
    val errorMsg: MutableLiveData<Event<String>> = MutableLiveData()
    val selectedChat =  MutableLiveData<Event<UsersEntity>>()

    var isDataAvailable: LiveData<Boolean> = Transformations.map(myContacts){
        it.isNotEmpty()
    }

    init {

    }

    override fun retry() {

    }

    override fun eventHandler(param: UsersEntity) {
        selectedChat.value = Event(param)
    }
}