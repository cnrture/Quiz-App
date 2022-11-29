package com.canerture.quizapp.domain.model.question

data class QuestionUI(
    val text: String,
    val correctAnswer: String,
    val allAnswers: MutableList<String>
)