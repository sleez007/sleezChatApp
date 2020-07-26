package ng.novacore.sleezchat.helper

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ng.novacore.sleezchat.utils.Constants.HAS_PROFILE_LOOKUP
import ng.novacore.sleezchat.utils.Constants.LOGGED_IN_LOOKUP
import ng.novacore.sleezchat.utils.Constants.MOBILE_NUMBER_LOOKUP
import ng.novacore.sleezchat.utils.Constants.TOKEN_LOOKUP
import ng.novacore.sleezchat.utils.Constants.USER_ID_LOOKUP
import javax.inject.Inject

class SharedPrefHelper @Inject constructor(@ApplicationContext context: Context, private val sharedPreferences: SharedPreferences, val gson: Gson, private val defDispatcher : CoroutineDispatcher = Dispatchers.Main){

    /**
     * @param telNo This is the phone number of the person who is running the app
     */
    fun saveMyNumber(telNo : String?)=sharedPreferences.edit().putString(MOBILE_NUMBER_LOOKUP, telNo).apply()
    fun getMyNumber(): String? = sharedPreferences.getString(MOBILE_NUMBER_LOOKUP, null)

    /**
     * @param token This is the user token as returned from the API call
     */
    fun saveToken(token:String?) = sharedPreferences.edit().putString(TOKEN_LOOKUP, token).apply()
    fun getToken():String? = sharedPreferences.getString(TOKEN_LOOKUP,null)

    /**
     * @param userID This is the user id of the person logged in
     */
    fun saveUserID(userID: String?) = sharedPreferences.edit().putString(USER_ID_LOOKUP, userID).apply()
    fun getUserID():String? = sharedPreferences.getString(USER_ID_LOOKUP,null)

    /**
     * @param isLoggedIn Boolean indicator to know if client is logged in
     */
    fun setIsLoggedIn(isLoggedIn: Boolean ) = sharedPreferences.edit().putBoolean(LOGGED_IN_LOOKUP, false).apply()
    fun isLoggedIn(): Boolean = sharedPreferences.getBoolean(LOGGED_IN_LOOKUP, false)

    /**
     * @param hasProfile Boolean parameter indicates the user has created a profile
     */
    fun toProfile(hasProfile: Boolean ) = sharedPreferences.edit().putBoolean(HAS_PROFILE_LOOKUP, false).apply()
    fun hasProfile(): Boolean = sharedPreferences.getBoolean(HAS_PROFILE_LOOKUP, false)





}