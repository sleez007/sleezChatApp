package ng.novacore.sleezchat.utils

object Constants {
    const val CHAT_CONTAINER_TO_CAMERA = 0
    const val CHAT_CONTAINER_TO_CHAT = 1
    const val CHAT_CONTAINER_TO_STATUS = 2
    const val CHAT_CONTAINER_TO_CALL = 3

    //PERMISSION REQUEST_CODE
    const val GRANT_MULTIPLE_PERMISSION_REQUEST_CODE = 360
    const val CAMERA_PERMISSION_REQUEST_CODE = 180


    //ACTIVITY RESULT CODE
    const val FCM_PHONE_AUTH_RESULT = 20
    const val REQUEST_IMAGE_FROM_GALLERY = 33
    const val REQUEST_IMAGE_FROM_CAMERA = 2

    //APP THEME
    const val THEME_LIGHT = "light"
    const val THEME_DARK = "dark"
    const val THEME_DEFAULT = "default"

    //sharedPref
    const val APP = "novachat"

    const val DATABASE_PAGE_SIZE = 50

    //sharedPref constants
    const val MOBILE_NUMBER_LOOKUP: String = "KEY_MOBILE_NUMBER"
    const val TOKEN_LOOKUP: String="KEY_TOKEN"
    const val USER_ID_LOOKUP : String = "KEY_USER_ID"
    const val LOGGED_IN_LOOKUP :String ="KEY_LOGGED_IN"
    const val HAS_PROFILE_LOOKUP: String ="KEY_HAS_PROFILE"
    const val SEEN_WIZARD_LOOKUP : String ="KEY_SEEN_WIZARD"
}