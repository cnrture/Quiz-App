package com.canerture.quizapp.data.source.remote

import com.canerture.quizapp.data.model.question.Result
import com.canerture.quizapp.data.model.token.Token
import retrofit2.http.GET
import retrofit2.http.Query

interface QuestionService {

    @GET("api_token.php?command=request")
    suspend fun getSessionToken(): Token

    @GET("api_token.php/")
    suspend fun resetSessionToken(
        @Query("command") command: String = "reset",
        @Query("token") token: String
    ): Token

    @GET("api.php/")
    suspend fun getQuestionsByCategory(
        @Query("amount") amount: Int = 10,
        @Query("category") category: Int,
        @Query("type") type: String,
        @Query("token") token: String
    ): Result
}