package ng.novacore.sleezchat.repository

import ng.novacore.sleezchat.internals.generics.GenericCb
import ng.novacore.sleezchat.model.auth.JoinModel
import ng.novacore.sleezchat.model.network.JoinModelResponse

interface VerificationRepository {
    //NETWORK RELATED
    suspend fun join(body: JoinModel, cb: GenericCb<JoinModelResponse>)
    suspend fun verifyOtp(body: JoinModel, cb:GenericCb<JoinModelResponse>)
    suspend fun resendOtp(body: JoinModel, cb:GenericCb<JoinModelResponse>)

}