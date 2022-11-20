package com.canerture.quizapp.data.model.category

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("trivia_categories")
    val triviaCategories: List<TriviaCategory>?
)