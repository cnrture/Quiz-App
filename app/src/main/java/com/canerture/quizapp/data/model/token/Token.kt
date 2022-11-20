package com.canerture.quizapp.data.model.token

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("response_code")
    val responseCode: Int,
    @SerializedName("response_message")
    val responseMessage: String?,
    val token: String?
)
