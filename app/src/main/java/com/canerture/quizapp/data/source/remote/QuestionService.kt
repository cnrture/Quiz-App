package com.canerture.quizapp.data.source.remote

import com.canerture.quizapp.data.model.question.Result
import com.canerture.quizapp.data.model.token.Token
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface QuestionService {

    @GET("api_token.php?command=request")
    fun retrieveSessionToken(): Observable<Token>

    @GET("api_token.php/")
    fun resetSessionToken(
        @Query("command") command: String = "reset",
        @Query("token") token: String
    ): Observable<Token>

    @GET("api.php/")
    fun getQuestionsByCategory(
        @Query("amount") amount: Int = 10,
        @Query("category") category: Int,
        @Query("type") type: String,
        @Query("token") token: String
    ): Observable<Result>
}