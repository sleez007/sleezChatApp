package ng.novacore.sleezchat.repository.remoteDataSource

import kotlinx.coroutines.Deferred
import ng.novacore.sleezchat.internals.exceptions.NoConnectivityException
import ng.novacore.sleezchat.model.SyncContacts
import ng.novacore.sleezchat.model.network.BaseResponse
import ng.novacore.sleezchat.model.network.SyncContactsResponse
import ng.novacore.sleezchat.network.ApiService
import ng.novacore.sleezchat.utils.Result
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class RemoteSourceImp @Inject constructor(private val apiService: ApiService):  RemoteSourceInterface{
    override suspend fun syncContacts(body: SyncContacts): Result<SyncContactsResponse> {
        val job = apiService.syncContactsAsync(body)
        return mapSimilarResponse(job) as Result<SyncContactsResponse>
    }

    private suspend fun mapSimilarResponse(job: Deferred<BaseResponse>): Result<BaseResponse>{
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