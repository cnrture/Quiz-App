package com.canerture.quizapp.data.model.category

import com.google.gson.annotations.SerializedName

data class TriviaCategory(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
)