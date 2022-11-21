package com.canerture.quizapp.data.source.remote

import com.canerture.quizapp.common.SessionManager
import com.canerture.quizapp.data.model.category.Category
import com.canerture.quizapp.data.model.question.Result
import com.canerture.quizapp.data.model.token.Token
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface QuestionService {

    @GET("api_token.php?command=request")
    fun retrieveSessionToken(): Observable<Token>

    @GET("api_category.php")
    fun getCategories(): Observable<Category>

    @GET("api.php/")
    fun getQuestionsByCategory(
        @Query("amount") amount: Int = 10,
        @Query("category") category: String,
        @Query("difficulty") difficulty: String,
        @Query("type") type: String,
        @Query("token") token: String = SessionManager.sessionToken
    ): Observable<Result>
}