package ng.novacore.sleezchat.activities.intro

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.helper.SharedPrefHelper
import ng.novacore.sleezchat.internals.enums.NavEnum
import ng.novacore.sleezchat.repository.VerificationRepository
import ng.novacore.sleezchat.utils.Event

class IntroViewModel @ViewModelInject constructor(
    private val repo: VerificationRepository,
    val sharedPrefHelper: SharedPrefHelper,
    @ApplicationContext val context: Context,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    //SPLASH LOGIC
    val toScreen = MutableLiveData<Event<NavEnum>>()
    init {
        nextScreen()
    }

    /**
     * @desc This method determines the next location where the user should be taking to
     */
    private fun nextScreen(){
        viewModelScope.launch {
            withContext(Dispatchers.Default){
                delay(1000)
                if(sharedPrefHelper.seenWizards()){
                    if(sharedPrefHelper.isLoggedIn()){
                        toScreen.postValue(Event(NavEnum.TO_MAIN))
                    }else{
                        if(sharedPrefHelper.hasProfile()){
                            toScreen.postValue(Event(NavEnum.TO_PROFILE))
                        }else{
                            toScreen.postValue(Event(NavEnum.TO_PHONE_NUM))
                        }
                    }
                }else{
                    toScreen.postValue(Event(NavEnum.TO_WIZARD))
                }
            }
        }
    }

    //WIZARD LOGIC
    var buttonWriteUp = MutableLiveData<String>(context.getString(R.string.proceed));

    fun setWriteUp(index: Int){

        if(index == 2 ){
            buttonWriteUp.value = context.getString(R.string.got_it)
        }else{
            buttonWriteUp.value = context.getString(R.string.proceed)
        }
    }

    /**
     * THIS METHOD IS TRIGGERED TO PROCEED FURTHER INTO THE APP
     */
    fun exitWizards(){
        sharedPrefHelper.setWizards(true)
        toScreen.value = Event(NavEnum.TO_PHONE_NUM)
    }


}