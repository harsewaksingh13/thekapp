package com.harsewak.kapp.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.harsewak.kapp.api.DefaultResponse

class LoginResponse : DefaultResponse<Token>() {
    @SerializedName("aux")
    @Expose
     var aux: Aux? = null

}

class Aux {
    @SerializedName("tokenPayload")
    @Expose
     var tokenPayload: TokenPayload? = null
}

class TokenPayload {
    @SerializedName("user_id")
    @Expose
     var userId: String? = null
}

class Token {
    @SerializedName("token")
    @Expose
     var token: String? = null
}