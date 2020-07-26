package ng.novacore.sleezchat.model.network

data class JoinModelResponse(
    var isSuccessful: Boolean,
    var hasProfile: Boolean,
    var token: String,
    var msg: String,
    var id: String
)