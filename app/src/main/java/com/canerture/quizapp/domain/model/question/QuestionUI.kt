package com.canerture.quizapp.domain.model.question

data class QuestionUI(
    val question: String,
    val correctAnswer: String,
    val incorrectAnswerOne: String,
    val incorrectAnswerTwo: String,
    val incorrectAnswerThree: String,
)