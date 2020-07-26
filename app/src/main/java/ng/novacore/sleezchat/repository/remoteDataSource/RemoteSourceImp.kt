package ng.novacore.sleezchat.repository.remoteDataSource

import ng.novacore.sleezchat.network.ApiInterface
import ng.novacore.sleezchat.network.VerificationService
import javax.inject.Inject

class RemoteSourceImp @Inject constructor(private val apiService: ApiInterface):  RemoteSourceInterface{

}