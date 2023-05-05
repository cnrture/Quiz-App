package com.canerture.quizapp.data.source.remote

import javax.inject.Inject

class QuestionDataSourceImpl @Inject constructor(
    private val questionService: QuestionService
) : QuestionDataSource {

    override suspend fun getQuestionsByCategory(category: Int, type: String, token: String) =
        questionService.getQuestionsByCategory(category, type, token)

    override suspend fun getSessionToken() = questionService.getSessionToken()
}