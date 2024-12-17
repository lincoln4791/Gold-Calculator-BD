package com.lincoln4791.goldcalculatorbd.ui.auth.signup.models

data class LoginResponse(
    var code: String?,
    var `data`: LoginData?,
    var is_success: Boolean?
)

data class LoginData(
    var login: LoginResult?
)

data class LoginResult(
    var access_token: String?,
    var created_at: Any?,
    var email: String?,
    var email_verified_at: Any?,
    var id: Int?,
    var isPremium: Int?,
    var name: String?,
    var updated_at: Any?
)