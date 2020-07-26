package ng.novacore.sleezchat.network

import kotlinx.coroutines.Deferred
import ng.novacore.sleezchat.application.EndPoints
import ng.novacore.sleezchat.model.auth.JoinModel
import ng.novacore.sleezchat.model.network.JoinModelResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface VerificationService {
    @POST(EndPoints.PHONE_VERIFICATION)
    fun joinAsync(@Body joinModel : JoinModel): Deferred<JoinModelResponse>

    @POST(EndPoints.OTP_VERIFICATION)
    fun verifyOtpAsync(@Body joinModel : JoinModel): Deferred<JoinModelResponse>

    @POST(EndPoints.RESEND_VERIFICATION)
    fun resendOtpAsync(@Body joinModel : JoinModel): Deferred<JoinModelResponse>
}