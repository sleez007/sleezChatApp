package ng.novacore.sleezchat.model.network

data class UploadResponse(
    var isSuccessful: Boolean,
    var msg: String,
    var fileName: String
)