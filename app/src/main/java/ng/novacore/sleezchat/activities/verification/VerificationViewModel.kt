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
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.helper.SharedPrefHelper
import ng.novacore.sleezchat.helper.camera.GalleryPicker
import ng.novacore.sleezchat.helper.camera.ImageManager
import ng.novacore.sleezchat.internals.enums.NavEnum
import ng.novacore.sleezchat.internals.enums.OpenMedia
import ng.novacore.sleezchat.internals.enums.VerificationEnum
import ng.novacore.sleezchat.internals.generics.GenericCb
import ng.novacore.sleezchat.model.auth.JoinModel
import ng.novacore.sleezchat.model.network.JoinModelResponse
import ng.novacore.sleezchat.model.network.UploadResponse
import ng.novacore.sleezchat.repository.VerificationRepository
import ng.novacore.sleezchat.utils.Event
import ng.novacore.sleezchat.utils.PhoneNumberUtility
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.util.*

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
    val toScreen = MutableLiveData<Event<NavEnum>>()

    init {
        toPage()
    }


    private fun toPage() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                if (sharedPrefHelper.hasProfile()) {
                    toScreen.postValue(Event(NavEnum.TO_PROFILE))
                }
            }
        }
    }


    fun clickHandler() {
        when (currentPage) {
            0 -> {
                _currentFrag.value = Event(1)
            }
            1 -> {
                _currentFrag.value = Event(2)
            }
            2->{
                _currentFrag.value = Event(3)
            }

        }
        Timber.i("The current page is $currentPage")
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
        if(loading.value == false){
            val joinModel = JoinModel(telNo = telNo, country = countryName)
            viewModelScope.launch {
                loading.postValue(true)
                repo.join(joinModel, telVerificationCb)
            }
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
            stopTimer()
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
    var byteArray: ByteArray? = null
    val displayName = MutableLiveData<String>()
    var path : String = ""

    /**
     * @param path : This is the file path to be decoded into a bitmap
     * @param targetH : The height of the view where this bitMap will be fitted into. This is to enable us resize the bitmap into a smaller fit
     * @param targetW : This is the width of the view where the bitmap will be fitted into. This is to enable us resize the bitmap into a smaller fit
     * @return Void
     * @desc : This method handles manipulation of image taking through mediastore camera. This method also  compresses the final version
     * of the image to be uploaded before upload
     */
    fun decodeImageOnBgThread(path: String, targetW: Int, targetH: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
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
                val bitM = BitmapFactory.decodeFile(path, bmOptions)
                bitMap.postValue(Event(bitM))
                byteArray= ImageManager.compressImage(path);
                this@VerificationViewModel.path = path
                Timber.i("----------------------------------")
                Timber.i(byteArray?.toString()?:"")
                Timber.i("----------------------------------")
            }
        }
    }

    /**
     * @param  Uri : This is the Image provider URI returned by the intent to MediaStore Gallery
     * @param activity :activity required by content provider
     * @return Void
     * @desc : This method handles manipulation of image gotten through Media store gallery Intent. This method also compresses the final version
     * of the image to be uploaded before upload
     */
    fun decodeGalleryUriToBitmap(photoUri: Uri, activity: Activity) {
        viewModelScope.launch {
            val bm = try {
               var bitM = GalleryPicker.loadImageFromUri(photoUri, activity)
                GalleryPicker.currentPhotoPath?.let {
                    byteArray = ImageManager.compressImage(it);
                    path = it
                }
                bitM

            } catch (ex: IOException) {
                throw ex
                null
            }
            bm?.let {
                bitMap.postValue(Event(it))
            }
        }
    }


    val profileCb: GenericCb<UploadResponse> = object : GenericCb<UploadResponse>{
        override fun success(resp: UploadResponse) {
            Timber.i(resp.toString())
            successMsg.postValue(Event(resp.msg))
            navigate.postValue(Event(VerificationEnum.TO_PROFILE_FRAG))
            loading.postValue(false)
        }

        override fun error(msg: String) {
            loading.postValue(false)
            errorMsg.postValue(Event(msg))
            Timber.i(msg)
        }

    }

    /**
     * Upload the information to the server
     */
     fun updateProfile(){
        if(loading.value== false){
            if(displayName.value==null || displayName.value!!.trim().length < 4) return warningMsg.postValue(Event(context.getString(R.string.display_name_msg)))
            if(byteArray == null) return warningMsg.postValue(Event(context.getString(R.string.provide_profile_photo)))
            // create RequestBody instance from file
            byteArray?.let {
                val requestFile: RequestBody? = RequestBody.create(MediaType.parse("image/*"), it)
                requestFile?.let {
                    val file : File = File(path);
                    val form = MultipartBody.Part.createFormData("file", file.name,it)
                    viewModelScope.launch {
                        loading.postValue(true)
                        repo.createProfileAsync(form,displayName.value!!.trim(), sharedPrefHelper.getUserID() ?: "",profileCb)
                    }

                }
            }
        }

    }





}