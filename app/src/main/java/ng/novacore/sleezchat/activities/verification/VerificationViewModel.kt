package ng.novacore.sleezchat.activities.verification

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.CountDownTimer
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ng.novacore.sleezchat.internals.enums.VerificationEnum
import ng.novacore.sleezchat.internals.generics.GenericCb
import ng.novacore.sleezchat.model.auth.JoinModel
import ng.novacore.sleezchat.model.network.JoinModelResponse
import ng.novacore.sleezchat.repository.VerificationRepository
import ng.novacore.sleezchat.utils.Event
import ng.novacore.sleezchat.utils.PhoneNumberUtility
import timber.log.Timber
import java.util.*
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.helper.SharedPrefHelper
import ng.novacore.sleezchat.helper.camera.GalleryPicker
import ng.novacore.sleezchat.internals.enums.OpenMedia
import java.io.IOException

class VerificationViewModel @ViewModelInject constructor(
    private val repo: VerificationRepository,
    val sharedPrefHelper: SharedPrefHelper,
    @ApplicationContext val context: Context,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val _currentFrag = MutableLiveData<Event<Int>>()
    val currentFrag: LiveData<Event<Int>> = _currentFrag
    var currentPage: Int = 0
    var navigateToHome = MutableLiveData<Event<Boolean>>()
    val navigate = MutableLiveData<Event<VerificationEnum>>()
    val uniqueID: String = UUID.randomUUID().toString()
    var loading = MutableLiveData<Boolean>(false)
    val successMsg = MutableLiveData<Event<String>>()
    val errorMsg = MutableLiveData<Event<String>>()
    val warningMsg = MutableLiveData<Event<String>>()


    fun clickHandler() {
        when (currentPage) {
            0 -> {
                _currentFrag.value = Event(1)
            }
            1 -> {
                _currentFrag.value = Event(2)
            }
        }
//        if(currentPage >= 2){
//            navigateToHome.value = Event(true)
//        }else{
//            _currentFrag.value = Event(++currentPage)
//        }
    }


    //PHONE NUMBER RELATED LOGIC
    var hasRequestedPhoneNumber: Boolean = false
    val setUpReceiver = MutableLiveData<Event<Boolean>>()
    var telNo: String = ""
    var telVerificationCb: GenericCb<JoinModelResponse> = object : GenericCb<JoinModelResponse> {
        override fun success(resp: JoinModelResponse) {
            Timber.i(resp.toString())
            setUpReceiver.postValue(Event(true))
            navigate.postValue(Event(VerificationEnum.TO_OTP_FRAG))
            loading.postValue(false)
            successMsg.postValue(Event(resp.msg))
        }

        override fun error(msg: String) {
            loading.postValue(false)
            errorMsg.postValue(Event(msg))
            Timber.i(msg)
        }
    }

    /**
     * @param telNo is the actual telephone number of the person
     * @param selectedCountryName is the name of the country
     * @param countryIso is the country character code i.e "NG"
     */
    fun validatePhoneNumber(telNo: String, countryIso: String, selectedCountryName: String) {
        this.telNo = telNo
        if (telNo.trim().isNotEmpty()) {
            val phoneNum = PhoneNumberUtility.getPhoneNumber(telNo, countryIso)
            if (PhoneNumberUtility.isValidPhoneNo(phoneNum)) {
                val internationalNum = PhoneNumberUtility.getInternationalNumber(phoneNum)
                val finalNumber = internationalNum.replace("-", "").replace(" ", "")
                Timber.i("$finalNumber is good")
                sharedPrefHelper.saveMyNumber(finalNumber)
                otp_code.value = null
                requestOtp(finalNumber, selectedCountryName)
            } else {
                warningMsg.value = Event(context.getString(R.string.invalid_phone))
            }

        } else {
            warningMsg.value = Event(context.getString(R.string.invalid_phone))
            //show a toast message saying invalid phone number
        }
    }

    /**
     * @param telNo is the actual telephone number of the person
     * @param countryName is the name of the country
     */
    private fun requestOtp(telNo: String, countryName: String) {
        val joinModel = JoinModel(telNo = telNo, country = countryName)
        viewModelScope.launch {
            loading.postValue(true)
            repo.join(joinModel, telVerificationCb)
        }

    }


    //OTP RELATED LOGIC
    var countDownTimer: CountDownTimer? = null
    val totalTimeCountInMilliseconds = 60 * 4 * 1000.toLong()
    val timeString = MutableLiveData<String>()
    var otp_code = MutableLiveData<String>()
    var otpVerificationCb: GenericCb<JoinModelResponse> = object : GenericCb<JoinModelResponse> {
        override fun success(resp: JoinModelResponse) {
            Timber.i(resp.toString())
            sharedPrefHelper.saveToken(resp.token)
            sharedPrefHelper.saveUserID(resp.id)
            sharedPrefHelper.toProfile(true)
            navigate.postValue(Event(VerificationEnum.TO_PROFILE_FRAG))
            loading.postValue(false)
            successMsg.postValue(Event(resp.msg))
        }

        override fun error(msg: String) {
            loading.postValue(false)
            errorMsg.postValue(Event(msg))
            Timber.i(msg)
        }

    }

    fun startTimer() {
        countDownTimer = MyTimer(totalTimeCountInMilliseconds, 500).start()
    }

    fun stopTimer() {
        countDownTimer?.cancel()
    }

    inner class MyTimer(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {
        override fun onFinish() {
        }

        override fun onTick(remainingTimeMs: Long) {
            val seconds: Long = remainingTimeMs / 1000
            timeString.value =
                String.format(Locale.getDefault(), "%02d", seconds / 60) + ":" + String.format(
                    Locale.getDefault(),
                    "%02d",
                    seconds % 60
                )
        }
    }

    /**
     * desc Handles OTP Verification
     */
    fun verifyOtp() {
        if (otp_code.value != null && otp_code.value?.length == 4) {
            viewModelScope.launch {
                loading.postValue(true)
                repo.verifyOtp(
                    JoinModel(otp_code = otp_code.value ?: "", telNo = telNo),
                    otpVerificationCb
                )
            }
        } else {
            warningMsg.value = Event(context.getString(R.string.invalid_otp))
        }
    }


    //PROFILE RELATED LOGIC
    val openMedia = MutableLiveData<Event<OpenMedia>>()
    val bitMap = MutableLiveData<Event<Bitmap>>()

    fun decodeImageOnBgThread(path:String, targetW: Int, targetH: Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val bmOptions = BitmapFactory.Options().apply {
                    // Get the dimensions of the bitmap
                    inJustDecodeBounds = true

                    val photoW: Int = outWidth
                    val photoH: Int = outHeight

                    // Determine how much to scale down the image
                    val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)

                    // Decode the image file into a Bitmap sized to fill the View
                    inJustDecodeBounds = false
                    inSampleSize = scaleFactor
                }
                val bitM = BitmapFactory.decodeFile(path,bmOptions)
                bitMap.postValue(Event(bitM))
            }
        }
    }

    fun decodeGalleryUriToBitmap(photoUri: Uri, activity: Activity){
        viewModelScope.launch {
            val bm = try{
                GalleryPicker.loadImageFromUri(photoUri, activity)
            }catch (ex: IOException){
               throw ex
                null
            }
            bm?.let {
                bitMap.postValue(Event(it))
            }

        }
    }


}