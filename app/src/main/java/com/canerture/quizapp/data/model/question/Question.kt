package com.canerture.quizapp.data.model.question

import com.google.gson.annotations.SerializedName

data class Question(
    @SerializedName("category")
    val category: String?,
    @SerializedName("correct_answer")
    val correctAnswer: String?,
    @SerializedName("difficulty")
    val difficulty: String?,
    @SerializedName("incorrect_answers")
    val incorrectAnswers: List<String?>?,
    @SerializedName("question")
    val question: String?,
    @SerializedName("type")
    val type: String?
)