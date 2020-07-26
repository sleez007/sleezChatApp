package ng.novacore.sleezchat.repository.remoteDataSource


import kotlinx.coroutines.Deferred
import ng.novacore.sleezchat.internals.exceptions.NoConnectivityException
import ng.novacore.sleezchat.model.auth.JoinModel
import ng.novacore.sleezchat.model.network.JoinModelResponse
import ng.novacore.sleezchat.network.VerificationService
import ng.novacore.sleezchat.utils.Result
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class VerificationRemoteInterfaceImpl @Inject constructor(private val verificationService: VerificationService) : VerificationRemoteInterface{
    override suspend fun join(body: JoinModel): Result<JoinModelResponse> {
        val  job = verificationService.joinAsync(body)
        return mapSimilarResponse(job)
    }

    override suspend fun verifyOtpAsync(body: JoinModel): Result<JoinModelResponse> {
        val job =  verificationService.verifyOtpAsync(body)
        return mapSimilarResponse(job)
    }

    override suspend fun resendOtpAsync(body: JoinModel): Result<JoinModelResponse> {
        val job =  verificationService.resendOtpAsync(body)
        return mapSimilarResponse(job)
    }

    private suspend fun mapSimilarResponse(job: Deferred<JoinModelResponse>): Result<JoinModelResponse>{
        return try{
            val response = job.await()
            when(response.isSuccessful){
                true->Result.Success(response)
                false->Result.Error(Exception(response.msg))
            }
        }catch (ex:Exception){
            manageErrors(ex)
        }
    }

    private fun manageErrors(ex: Exception): Result.Error{
        val msg = when(ex){
            is NoConnectivityException ->{
                "No Internet Connectivity"
            }
            is IOException ->{
                "Could not connect, Please try again"
            }
            is HttpException ->{
                when(ex.code()){
                    422 ->{
                        "Unable to create/modify resource"
                    }
                    else-> ex.message()
                }
            }
            else -> {
                ex.message
                "Please Try again ${ex.message}"
            }

        }
        return Result.Error(Exception(msg))
    }

}