package ng.novacore.sleezchat.model.network

import ng.novacore.sleezchat.model.data.MyContacts
import ng.novacore.sleezchat.model.network.BaseResponse

data class SyncContactsResponse( val contacts : List<MyContacts>): BaseResponse()