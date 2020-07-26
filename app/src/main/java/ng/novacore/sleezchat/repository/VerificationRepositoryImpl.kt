package ng.novacore.sleezchat.repository


import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ng.novacore.sleezchat.R
import ng.novacore.sleezchat.internals.generics.GenericCb
import ng.novacore.sleezchat.model.auth.JoinModel
import ng.novacore.sleezchat.model.network.JoinModelResponse
import ng.novacore.sleezchat.repository.localDataSource.VerificationLocalInterface
import ng.novacore.sleezchat.repository.remoteDataSource.VerificationRemoteInterface
import ng.novacore.sleezchat.utils.Result
import javax.inject.Inject

class VerificationRepositoryImpl @Inject constructor(val remoteSource: VerificationRemoteInterface, private val localSourceInterface: VerificationLocalInterface, @ApplicationContext val context :Context) : VerificationRepository{
    override suspend fun join(body: JoinModel, cb: GenericCb<JoinModelResponse>) = cbSimilarities(remoteSource.join(body),cb)

    override suspend fun verifyOtp(body: JoinModel, cb: GenericCb<JoinModelResponse>) = cbSimilarities(remoteSource.verifyOtpAsync(body),cb)

    override suspend fun resendOtp(body: JoinModel, cb: GenericCb<JoinModelResponse>) = cbSimilarities(remoteSource.resendOtpAsync(body),cb)

    private fun cbSimilarities(response: Result<JoinModelResponse>, cb: GenericCb<JoinModelResponse>) {
        when(response){
            is Result.Success->cb.success(response.data)
            is Result.Error-> cb.error(response.exception.message ?: context.getString(R.string.network_error))
        }
    }


}