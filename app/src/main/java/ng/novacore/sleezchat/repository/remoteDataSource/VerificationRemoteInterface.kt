package ng.novacore.sleezchat.repository.remoteDataSource

import ng.novacore.sleezchat.model.auth.JoinModel
import ng.novacore.sleezchat.model.network.JoinModelResponse
import ng.novacore.sleezchat.utils.Result

interface VerificationRemoteInterface {
    suspend fun join(body: JoinModel): Result<JoinModelResponse>
    suspend fun verifyOtpAsync(body: JoinModel): Result<JoinModelResponse>
    suspend fun resendOtpAsync(body: JoinModel): Result<JoinModelResponse>
}