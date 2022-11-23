package com.canerture.quizapp.mapper

import com.canerture.quizapp.data.model.question.Question
import com.canerture.quizapp.domain.model.question.QuestionUI

fun List<Question>.toQuestionUIList() = map {
    QuestionUI(
        text = it.text.orEmpty(),
        correctAnswer = it.correctAnswer.orEmpty(),
        incorrectAnswerOne = it.incorrectAnswers?.getOrNull(0).orEmpty(),
        incorrectAnswerTwo = it.incorrectAnswers?.getOrNull(1).orEmpty(),
        incorrectAnswerThree = it.incorrectAnswers?.getOrNull(2).orEmpty(),
    )
}