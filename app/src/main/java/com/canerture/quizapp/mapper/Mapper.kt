package com.canerture.quizapp.mapper

import com.canerture.quizapp.data.model.question.Question
import com.canerture.quizapp.domain.model.question.QuestionUI

fun Question.toQuestionUI() = QuestionUI(
    question = question.orEmpty(),
    correctAnswer = correctAnswer.orEmpty(),
    incorrectAnswerOne = incorrectAnswers?.getOrNull(0).orEmpty(),
    incorrectAnswerTwo = incorrectAnswers?.getOrNull(1).orEmpty(),
    incorrectAnswerThree = incorrectAnswers?.getOrNull(2).orEmpty(),
)