package ng.novacore.sleezchat.repository.remoteDataSource

import ng.novacore.sleezchat.model.auth.JoinModel
import ng.novacore.sleezchat.model.network.JoinModelResponse
import ng.novacore.sleezchat.model.network.UploadResponse
import ng.novacore.sleezchat.utils.Result
import okhttp3.MultipartBody
import retrofit2.http.Part

interface VerificationRemoteInterface {
    suspend fun join(body: JoinModel): Result<JoinModelResponse>
    suspend fun verifyOtpAsync(body: JoinModel): Result<JoinModelResponse>
    suspend fun resendOtpAsync(body: JoinModel): Result<JoinModelResponse>
    suspend fun createProfileAsync(displayPic : MultipartBody.Part, @Part("displayName") displayName: String,@Part("uid") uId: String):Result<UploadResponse>
}