package ng.novacore.sleezchat.model.auth

data class JoinModel(
    var telNo: String ="",
    var country: String ="",
    var fbToken: String ="",
    var access_token : String= "",
    var account_id: String = "",
    var otp_code : String =""
)