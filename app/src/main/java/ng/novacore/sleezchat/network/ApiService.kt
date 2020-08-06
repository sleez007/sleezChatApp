package ng.novacore.sleezchat.network

import kotlinx.coroutines.Deferred
import ng.novacore.sleezchat.application.EndPoints
import ng.novacore.sleezchat.model.SyncContacts
import ng.novacore.sleezchat.model.network.SyncContactsResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST(EndPoints.SYNC_ALL_CONTACTACTS)
    fun syncContactsAsync(@Body contact: SyncContacts): Deferred<SyncContactsResponse>
}