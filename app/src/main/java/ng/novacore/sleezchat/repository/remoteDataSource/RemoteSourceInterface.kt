package ng.novacore.sleezchat.repository.remoteDataSource

import ng.novacore.sleezchat.model.SyncContacts
import ng.novacore.sleezchat.model.network.SyncContactsResponse
import ng.novacore.sleezchat.utils.Result

interface RemoteSourceInterface{
    suspend fun syncContacts(body: SyncContacts): Result<SyncContactsResponse>
}