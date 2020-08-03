package ng.novacore.sleezchat.network

import kotlinx.coroutines.Deferred
import ng.novacore.sleezchat.model.SyncContacts
import ng.novacore.sleezchat.model.network.SyncContactsResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("")
    fun syncContactsAsync(@Body contact: SyncContacts): Deferred<SyncContactsResponse>
}