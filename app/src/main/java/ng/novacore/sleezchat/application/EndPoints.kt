package ng.novacore.sleezchat.application

object EndPoints {

    //AUTHORIZATION ENDPOINTS
    const val PHONE_VERIFICATION = "auth/phone_verification"
    const val OTP_VERIFICATION = "/auth/otp_verification"
    const val RESEND_VERIFICATION = "/auth/resend_otp"
    const val CREATE_PROFILE = "/auth/profile/{uId}/{displayName}"

    //NORMAL ENDPOINTS
    const val SYNC_ALL_CONTACTACTS ="/users/all"
}