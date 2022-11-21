package com.canerture.quizapp.data.model.question

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("response_code")
    val responseCode: Int?,
    @SerializedName("results")
    val questions: List<Question>?
)