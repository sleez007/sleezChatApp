package ng.novacore.sleezchat.repository

import ng.novacore.sleezchat.repository.localDataSource.LocalSourceInterface
import ng.novacore.sleezchat.repository.remoteDataSource.RemoteSourceInterface
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(val remoteSource: RemoteSourceInterface, val localSourceInterface: LocalSourceInterface): AppRepositoryInterface {

}